package app.personalfinance.viewModel.transfers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import app.personalfinance.data.transfers.TransferModel;
import app.personalfinance.data.transfers.TransferWithDetails;
import app.personalfinance.repo.transfers.TransfersRepository;

public class TransfersViewModel extends AndroidViewModel {

    private TransfersRepository repository;

    private final MutableLiveData<String> mText;

    public TransfersViewModel(@NonNull Application application) {
        super(application);
        repository = new TransfersRepository(application);

        mText = new MutableLiveData<>();
        mText.setValue("IncomesViewModel");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void insertTransfer(int accountFrom, int accountTo, double amount, String description, String date) {
        repository.insert(new TransferModel(accountFrom, accountTo, amount, description, date));
    }

    public void deleteTransfer(TransferModel transfer) {
        repository.delete(transfer);
    }

    public LiveData<List<TransferWithDetails>> getAllTransfers() {
        return repository.getAllTransfers();
    }
}