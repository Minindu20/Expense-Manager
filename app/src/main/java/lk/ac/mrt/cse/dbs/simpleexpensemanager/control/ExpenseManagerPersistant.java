package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;


public class ExpenseManagerPersistant extends ExpenseManager {
    public ExpenseManagerPersistant(Context context){
        super(context);
        setup(context);
    }
    @Override
    public void setup(Context con) {
        TransactionDAO transactionDAOPersistent = new PersistentTransactionDAO(con);
        setTransactionsDAO(transactionDAOPersistent);
        AccountDAO accountDAOPersistent = new PersistentAccountDAO(con);
        setAccountsDAO(accountDAOPersistent);
    }
}
