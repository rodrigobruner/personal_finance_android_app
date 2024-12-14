package app.personalfinance.repo.accounts;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
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
        Handler mainHandler = new Handler(Looper.getMainLooper()); // Handler to run on the main thread
        executor.execute(() -> { // Execute the task in a new thread
            try {
                accountDao.insert(account); // Insert the account
                mainHandler.post(() -> { // Run on the main thread
                    callback.accept(true); // Return true to the callback function
                });
            } catch (Exception e) { // Catch any exception
                mainHandler.post(() -> { // Run on the main thread
                    callback.accept(false); // Return false to the callback function
                });
                e.printStackTrace();
            }
        });
    }

    // Function to update an account, uses the same logic as insert
    public void update(AccountModel account, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                AccountModel accountOriginal = accountDao.getAccount(account.getId());
                if (accountOriginal != null) {
                    double difference = account.getBalance() - accountOriginal.getBalance();
                    account.setCurrentBalance(accountOriginal.getCurrentBalance() + difference);
                    accountDao.update(account);
                    mainHandler.post(() -> {
                        callback.accept(true);
                    });
                } else {
                    mainHandler.post(() -> {
                        callback.accept(false);
                    });
                }
            } catch (Exception e) {
                mainHandler.post(() -> {
                    callback.accept(false);
                });
                e.printStackTrace();
            }
        });
    }

    // Function to update an account, uses the same logic as insert
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

    // Function to delete an account, uses the same logic as insert
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
            return accountDao.getLast(); // Get the last account
        } catch (Exception e) { // Catch any exception
            e.printStackTrace();
            return null; // Return null
        }
    }

    // Function to get an account, uses the same logic as AccountModel
    public AccountModel getAccount(int id) {
        try {
            return accountDao.getAccount(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Function to get all accounts, uses the same logic as AccountModel
    public LiveData<List<AccountModel>> getAllAccounts() {
        try {
            return accountDao.getAllAccounts();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}