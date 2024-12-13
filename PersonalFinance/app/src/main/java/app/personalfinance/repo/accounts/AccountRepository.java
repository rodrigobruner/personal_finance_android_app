package app.personalfinance.repo.accounts;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import app.personalfinance.data.PersonalFinanceDatabase;
import app.personalfinance.data.accounts.AccountDao;
import app.personalfinance.data.accounts.AccountModel;

public class AccountRepository {
    // Dao instance
    private AccountDao accountDao;

    // Number of threads in the pool
    private static final int NUMBER_OF_THREADS = 2;
    //Flag to check if there is an error
    private final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    // Constructor
    public AccountRepository(Application application) {
        PersonalFinanceDatabase database = PersonalFinanceDatabase.getInstance(application);
        // Set dao
        accountDao = database.accountDao();
    }


    // Function to insert an account
    public void insert(AccountModel account, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                accountDao.insert(account);
                mainHandler.post(() -> {
                    callback.accept(true);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    callback.accept(false);
                });
                e.printStackTrace();
            }
        });
    }

    // Function to update an account
    public void updateAccountBalance(int accountID, double amount, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                accountDao.updateAccountBalance(accountID, amount);
                mainHandler.post(() -> {
                    callback.accept(true);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    callback.accept(false);
                });
                e.printStackTrace();
            }
        });
    }

    // Function to delete an account
    public void delete(AccountModel account, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                accountDao.delete(account);
                mainHandler.post(() -> {
                    callback.accept(true);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    callback.accept(false);
                });
                e.printStackTrace();
            }
        });
    }

    // Function to get the last inserted account
    public AccountModel getLast() {
        try {
            return accountDao.getLast();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Function to get all accounts
    public LiveData<List<AccountModel>> getAllAccounts() {
        try {
            return accountDao.getAllAccounts();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}