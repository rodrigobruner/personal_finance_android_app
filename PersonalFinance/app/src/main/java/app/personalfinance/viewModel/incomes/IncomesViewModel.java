package app.personalfinance.viewModel.incomes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class IncomesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public IncomesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("IncomesViewModel");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
