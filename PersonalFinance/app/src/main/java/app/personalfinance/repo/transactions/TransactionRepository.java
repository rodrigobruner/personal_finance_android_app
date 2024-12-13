package app.personalfinance.repo.transactions;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.personalfinance.data.PersonalFinanceDatabase;
import app.personalfinance.data.helpper.DataChartLabelValue;
import app.personalfinance.data.transactions.TransactionDAO;
import app.personalfinance.data.transactions.TransactionModel;
import app.personalfinance.data.transactions.TransactionWithDetails;

public class TransactionRepository {
    // Dao instance
    private TransactionDAO transactionDao;
    // Number of threads in the pool
    private static final int NUMBER_OF_THREADS = 2;
    // Instance to execute the queries in background
    private final ExecutorService dbExecutor;

    // Constructor
    public TransactionRepository(Application application) {
        PersonalFinanceDatabase database = PersonalFinanceDatabase.getInstance(application);
        // Set dao
        transactionDao = database.transactionDao();

        // Set executor service
        dbExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }

    // Function to insert a transaction
    public void insert(TransactionModel model) {
        // Run in background the insert command
        dbExecutor.execute(() -> transactionDao.insert(model));
    }

    // Function to delete a transaction
    public void delete(TransactionModel model) {
        dbExecutor.execute(() ->
                transactionDao.delete(model));
    }

    // Function to get all transactions
    public LiveData<List<TransactionWithDetails>> getAllTransactions() {
        return transactionDao.getAllTransactions();
    }

    // Function to get all incomes
    public LiveData<List<TransactionWithDetails>> getAllIncomes() {
        return transactionDao.getAllIncomes();
    }

    // Function to get all expenses
    public LiveData<List<TransactionWithDetails>> getAllExpenses() {
        return transactionDao.getAllExpenses();
    }


    // Function to get the current month summary by type
    public LiveData<List<DataChartLabelValue>> getCurrentMonthSummaryByType(String type) {
        return transactionDao.getCurrentMonthSummaryByType(type);
    }
}