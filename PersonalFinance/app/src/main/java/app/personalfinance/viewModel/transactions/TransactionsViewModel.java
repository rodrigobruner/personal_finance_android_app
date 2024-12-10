package app.personalfinance.viewModel.transactions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class TransactionsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TransactionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("TransactionsViewModel");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
