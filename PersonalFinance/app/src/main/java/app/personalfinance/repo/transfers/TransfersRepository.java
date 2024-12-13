package app.personalfinance.repo.transfers;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import app.personalfinance.data.PersonalFinanceDatabase;
import app.personalfinance.data.transfers.TransferDao;
import app.personalfinance.data.transfers.TransferModel;
import app.personalfinance.data.transfers.TransferWithDetails;

public class TransfersRepository {
    // Dao instance
    private TransferDao transferDao;

    // Number of threads in the pool
    private static final int NUMBER_OF_THREADS = 2;
    //Flag to check if there is an error
    private final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    // Constructor
    public TransfersRepository(Application application) {
        PersonalFinanceDatabase database = PersonalFinanceDatabase.getInstance(application);
        // Set dao
        transferDao = database.transferDao();
    }

    // Function to insert a transfer
    public void insert(TransferModel transfer, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                transferDao.insert(transfer);
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

    // Function to update a transfer
    public void update(TransferModel transfer, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                transferDao.update(transfer);
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

    // Function to delete a transfer
    public void delete(TransferModel transfer, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                transferDao.delete(transfer);
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

    // Function to get all transfers
    public LiveData<List<TransferWithDetails>> getAllTransfers() {
        try {
            return transferDao.getAllTransfers();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}