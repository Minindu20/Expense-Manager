package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseManager.ACCOUNT_NUMBER;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseManager.ACCOUNT_TABLE;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseManager.BANK_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseManager.HOLDERS_NAME;
import static lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DataBaseManager.INITIAL_BALANCE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private final DataBaseManager databaseManager;
    private SQLiteDatabase SQLdatabase;
    public PersistentAccountDAO(Context context){
        databaseManager = new DataBaseManager(context);
    }

    public Account getAccount(String accountNum) throws InvalidAccountException {

        SQLdatabase = databaseManager.getReadableDatabase();
        Cursor cursor = SQLdatabase.query(
                ACCOUNT_TABLE,   // process the query
                new String[]{ACCOUNT_NUMBER, BANK_NAME, HOLDERS_NAME, INITIAL_BALANCE}, ACCOUNT_NUMBER + " = ?", new String[]{accountNum}, null, null, null
        );

        if (cursor != null){
            cursor.moveToFirst();

            Account account = new Account(accountNum, cursor.getString(cursor.getColumnIndex(BANK_NAME)),
                    cursor.getString(cursor.getColumnIndex(HOLDERS_NAME)), cursor.getDouble(cursor.getColumnIndex(INITIAL_BALANCE)));
            return account;

        }
        else {
            String exceptionMessage = "Account " + accountNum + " is invalid.";
            throw new InvalidAccountException(exceptionMessage);
        }
    }

    public List<String> getAccountNumbersList(){
        SQLdatabase = databaseManager.getReadableDatabase();
        List<String> accountNumbersList = new ArrayList<String>();
        Cursor cursor = SQLdatabase.query(
                ACCOUNT_TABLE,   // Query to get Account number
                new String[]{ACCOUNT_NUMBER}, null, null, null, null, null
        );

        while(cursor.moveToNext()) {
            String accountNum = cursor.getString(
                    cursor.getColumnIndexOrThrow(ACCOUNT_NUMBER));
            accountNumbersList.add(accountNum);
        }
        cursor.close();
        return accountNumbersList;
    }
    public List<Account> getAccountsList() {

        SQLdatabase = databaseManager.getReadableDatabase();
        List<Account> accountsList = new ArrayList<Account>();
        Cursor cursor = SQLdatabase.query(
                ACCOUNT_TABLE,   // generate quary to get the columns
                new String[]{ACCOUNT_NUMBER, BANK_NAME, HOLDERS_NAME, INITIAL_BALANCE}, null, null, null, null, null
        );

        while(cursor.moveToNext()) {
            String accountNum = cursor.getString(cursor.getColumnIndex(ACCOUNT_NUMBER));
            String bankName = cursor.getString(cursor.getColumnIndex(BANK_NAME));
            String holdersName = cursor.getString(cursor.getColumnIndex(HOLDERS_NAME));
            double initialBalance = cursor.getDouble(cursor.getColumnIndex(INITIAL_BALANCE));
            Account account = new Account(accountNum,bankName,holdersName,initialBalance);
            accountsList.add(account);
        }
        cursor.close();
        return accountsList;
    }


    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLdatabase = databaseManager.getWritableDatabase();
        SQLdatabase.delete(ACCOUNT_TABLE,ACCOUNT_NUMBER + " = ?",new String[]{accountNo});
        SQLdatabase.close();
    }
    @Override
    public void addAccount(Account account) {
        SQLdatabase = databaseManager.getWritableDatabase();
        ContentValues rowValues = new ContentValues();
        rowValues.put(ACCOUNT_NUMBER,account.getAccountNo());
        rowValues.put(BANK_NAME,account.getBankName());
        rowValues.put(HOLDERS_NAME,account.getAccountHolderName());
        rowValues.put(INITIAL_BALANCE,account.getBalance());
        //inserting
        SQLdatabase.insert(ACCOUNT_TABLE,null,rowValues);
        SQLdatabase.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

         SQLdatabase = databaseManager.getWritableDatabase();
         Cursor cursor = SQLdatabase.query(  //process the query
                 ACCOUNT_TABLE, new String[]{INITIAL_BALANCE}, ACCOUNT_NUMBER + " = ?", new String[]{accountNo}, null, null, null
         );
        double balance;
         if(cursor.moveToFirst()){
             balance = cursor.getDouble(0);
         }
         else{
             String invalid = "Account " + accountNo + "is invalid";
             throw new InvalidAccountException(invalid);
         }

         ContentValues rowValues = new ContentValues();
         if(expenseType == ExpenseType.INCOME){
             rowValues.put(INITIAL_BALANCE,balance+amount);
         }
         else{
             rowValues.put(INITIAL_BALANCE, balance - amount);
         }
         SQLdatabase.update(ACCOUNT_TABLE,rowValues,ACCOUNT_NUMBER + " =?", new String[] {accountNo});
         cursor.close();
         SQLdatabase.close();

    }


}


