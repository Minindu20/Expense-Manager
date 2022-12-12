package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseManager.ACCOUNT_NUMBER;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseManager.AMOUNT;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseManager.DATE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseManager.EXPENSE_TYPE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseManager.TRANSACTION_TABLE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private final DataBaseManager databaseManager;
    private SQLiteDatabase SQLdatabase;
    public PersistentTransactionDAO(Context con) {
        databaseManager = new DataBaseManager(con);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLdatabase = databaseManager.getWritableDatabase();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ContentValues rowValues = new ContentValues();
        rowValues.put(ACCOUNT_NUMBER,accountNo);
        rowValues.put(DATE, dateFormat.format(date));
        rowValues.put(AMOUNT,amount);
        rowValues.put(EXPENSE_TYPE,String.valueOf(expenseType));
        SQLdatabase.insert(TRANSACTION_TABLE,null,rowValues);
        SQLdatabase.close();

    }

    @Override
    public List<Transaction> getAllTransactionLogs() throws ParseException {
        List<Transaction> transactionsList = new ArrayList<>();
        SQLdatabase = databaseManager.getReadableDatabase();
        Cursor cursor = SQLdatabase.query( //generate quary
                TRANSACTION_TABLE,
                new String[]{ DATE, ACCOUNT_NUMBER, EXPENSE_TYPE, AMOUNT}, null, null, null, null, null
        );
        while(cursor.moveToNext()){
            String dateString = cursor.getString(cursor.getColumnIndex(DATE));
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
            String account = cursor.getString(cursor.getColumnIndex(ACCOUNT_NUMBER));
            ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(EXPENSE_TYPE)));
            double amount = cursor.getDouble(cursor.getColumnIndex(AMOUNT));
            Transaction newTransaction = new Transaction(date,account,expenseType,amount);
            transactionsList.add(newTransaction);
        }
        cursor.close();
        return transactionsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) throws ParseException {
        List<Transaction> transactionsList = new ArrayList<>();
        SQLdatabase = databaseManager.getReadableDatabase();
        Cursor cursor = SQLdatabase.query( //generate quary
                TRANSACTION_TABLE,
                new String[]{DATE, ACCOUNT_NUMBER, EXPENSE_TYPE, AMOUNT}, null, null, null, null, null
        );
        int size = cursor.getCount();
        while(cursor.moveToNext()){
            String dateString = cursor.getString(cursor.getColumnIndex(DATE));
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateString);
            String account = cursor.getString(cursor.getColumnIndex(ACCOUNT_NUMBER));
            ExpenseType expense = ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(EXPENSE_TYPE)));
            double amount = cursor.getDouble(cursor.getColumnIndex(AMOUNT));
            Transaction newTransaction = new Transaction(date,account,expense,amount);
            transactionsList.add(newTransaction);
        }
        if(size<=limit){
            return transactionsList;
        }
        return transactionsList.subList(size-limit,size);


    }
}
