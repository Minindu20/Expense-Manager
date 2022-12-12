package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "200206P.sqllite";

    // HERE I BASICALY CREATE 2 TABLES
    // ACCOUNT_TABLE
    //TRANSACTION_TABLE
    public static final String ACCOUNT_TABLE ="account" ;
    public static final String TRANSACTION_TABLE = "transactiontable";
    public static final String ACCOUNT_NUMBER = "accountNo";
    //ACCOUNT TABLE - COLUMN NAMES
    public static final String BANK_NAME = "bankName";
    public static final String HOLDERS_NAME = "HoldersName";
    public static final String INITIAL_BALANCE = "balance";

    //TRANSACTION TABLE - COLUMN NAMES
    public static final String ID = "id";
    public static final String DATE = "date";
    public static final String EXPENSE_TYPE = "expType";
    public static final String AMOUNT = "amount";

    public DataBaseManager(Context con){
        super(con,DB_NAME,null,1);
    }

    public void onCreate(SQLiteDatabase database){
        database.execSQL("CREATE TABLE " + ACCOUNT_TABLE + "(" +
                ACCOUNT_NUMBER + " TEXT PRIMARY KEY, " +
                BANK_NAME + " TEXT NOT NULL, " +
                HOLDERS_NAME + " TEXT NOT NULL, " +
                INITIAL_BALANCE + " REAL NOT NULL)");

        database.execSQL("CREATE TABLE " + TRANSACTION_TABLE + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE + " TEXT NOT NULL, " +
                EXPENSE_TYPE + " TEXT NOT NULL, " +
                AMOUNT + " REAL NOT NULL, " +
                ACCOUNT_NUMBER + " TEXT," +
                "FOREIGN KEY (" + ACCOUNT_NUMBER + ") REFERENCES " + ACCOUNT_TABLE + "(" + ACCOUNT_NUMBER + "))");
    }
    public void onUpgrade(SQLiteDatabase database,int old,int newV){
        database.execSQL("DROP TABLE IF EXISTS " + ACCOUNT_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + TRANSACTION_TABLE);

        onCreate(database);
    }
}
