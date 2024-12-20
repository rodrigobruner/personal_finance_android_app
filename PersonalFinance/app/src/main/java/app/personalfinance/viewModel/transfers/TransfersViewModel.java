package app.personalfinance.viewModel.transfers;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import java.util.function.Consumer;

import app.personalfinance.data.transfers.TransferModel;
import app.personalfinance.data.transfers.TransferWithDetails;
import app.personalfinance.repo.transfers.TransfersRepository;

public class TransfersViewModel extends AndroidViewModel {
    // repository
    private TransfersRepository repository;
    // constructor
    public TransfersViewModel(@NonNull Application application) {
        super(application);
        // initialize repository
        repository = new TransfersRepository(application);
    }

    // insert transfer
    public void insertTransfer(int accountFrom, int accountTo, double amount, String description, String date,  Consumer<Boolean> callback) {
        repository.insert(new TransferModel(accountFrom, accountTo, amount, description, date), callback);
    }

    // delete transfer
    public void deleteTransfer(TransferModel transfer, Consumer<Boolean> callback) {
        repository.delete(transfer, callback);
    }

    // get all transfers
    public LiveData<List<TransferWithDetails>> getAllTransfers() {
        return repository.getAllTransfers();
    }
}