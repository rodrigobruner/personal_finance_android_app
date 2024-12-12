package app.personalfinance.repo.transfers;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.personalfinance.data.PersonalFinanceDatabase;
import app.personalfinance.data.transfers.TransferDao;
import app.personalfinance.data.transfers.TransferModel;
import app.personalfinance.data.transfers.TransferWithDetails;

public class TransfersRepository {
    // Dao instance
    private TransferDao transferDao;
    // Number of threads in the pool
    private static final int NUMBER_OF_THREADS = 2;
    // Instance to execute the queries in background
    private final ExecutorService dbExecutor;

    // Constructor
    public TransfersRepository(Application application) {
        PersonalFinanceDatabase database = PersonalFinanceDatabase.getInstance(application);
        // Set dao
        transferDao = database.transferDao();

        // Set executor service
        dbExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }

    // Function to insert a transfer
    public void insert(TransferModel model) {
        // Run in background the insert command
        dbExecutor.execute(() -> transferDao.insert(model));
    }

    // Function to delete a transfer
    public void delete(TransferModel model) {
        dbExecutor.execute(() ->
                transferDao.delete(model));
    }

    // Function to get all transfers
    public LiveData<List<TransferWithDetails>> getAllTransfers() {
        return transferDao.getAllTransfers();
    }
}