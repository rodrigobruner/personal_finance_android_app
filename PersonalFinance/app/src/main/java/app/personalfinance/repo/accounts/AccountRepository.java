package app.personalfinance.repo.accounts;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.personalfinance.data.accounts.AccountDao;
import app.personalfinance.data.accounts.AccountDatabase;
import app.personalfinance.data.accounts.AccountModel;

public class AccountRepository {
    // Dao instance
    private AccountDao accountDao;
    // Number of threads in the pool
    private static final int NUMBER_OF_THREADS = 2;
    // Instance to execute the queries in background
    private final ExecutorService dbExecutor;

    // Constructor
    public AccountRepository(Application application) {
        AccountDatabase database = AccountDatabase.getInstance(application);
        // Set dao
        accountDao = database.accountDao();

        // Set executor service
        dbExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }

    // Function to insert account
    public void insert(AccountModel model) {
        // Run in background the insert command
        dbExecutor.execute(() ->
                accountDao.insert(model));
    }

    public void delete(AccountModel account) {
        dbExecutor.execute(() ->
            accountDao.delete(account));
    }

    // Function to get the last inserted account
    public AccountModel getLast() {
        return accountDao.getLast();
    }

    public LiveData<List<AccountModel>> getAllAccounts() {
        return accountDao.getAllAccounts();
    }
}