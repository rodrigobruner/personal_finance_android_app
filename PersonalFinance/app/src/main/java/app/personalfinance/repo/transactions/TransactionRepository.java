package app.personalfinance.repo.transactions;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import app.personalfinance.data.PersonalFinanceDatabase;
import app.personalfinance.data.helpper.DataChartLabelValueModel;
import app.personalfinance.data.transactions.TransactionDAO;
import app.personalfinance.data.transactions.TransactionModel;
import app.personalfinance.data.transactions.TransactionWithDetails;

public class TransactionRepository {
    // Dao instance
    private TransactionDAO transactionDao;

    // Number of threads in the pool
    private static final int NUMBER_OF_THREADS = 2;
    //Flag to check if there is an error
    private final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    // Constructor
    public TransactionRepository(Application application) {
        PersonalFinanceDatabase database = PersonalFinanceDatabase.getInstance(application);
        // Set dao
        transactionDao = database.transactionDao();
    }

    // Function to insert a transaction
    public void insert(TransactionModel transaction, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper()); // Handler to run on the main thread
        executor.execute(() -> { // Execute the task in a new thread
            try {
                transactionDao.insert(transaction); // Insert the transaction
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

    // Function to update a transaction, uses the same logic as insert
    public void update(TransactionModel transaction, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                transactionDao.update(transaction);
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

    // Function to delete a transaction, uses the same logic as insert
    public void delete(TransactionModel transaction, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                transactionDao.delete(transaction);
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

    // Function to get all transactions
    public LiveData<List<TransactionWithDetails>> getAllTransactions() {
        try {
            return transactionDao.getAllTransactions(); // Return all transactions
        } catch (Exception e) { // Catch any exception
            e.printStackTrace();
            return null; // Return null
        }
    }

    // Function to get all incomes, uses the same logic as getAllTransactions
    public LiveData<List<TransactionWithDetails>> getAllIncomes() {
        try {
            return transactionDao.getAllIncomes();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Function to get all expenses, uses the same logic as getAllTransactions
    public LiveData<List<TransactionWithDetails>> getAllExpenses() {
        try {
            return transactionDao.getAllExpenses();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    // Function to get the current month summary by type, uses the same logic as getAllTransactions
    public LiveData<List<DataChartLabelValueModel>> getCurrentMonthSummaryByType(String type) {
        try {
            return transactionDao.getCurrentMonthSummaryByType(type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}