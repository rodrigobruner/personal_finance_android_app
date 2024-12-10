package app.personalfinance.viewModel.expenses;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ExpensesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public ExpensesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("ExpensesViewModel");
    }

    public LiveData<String> getText() {
        return mText;
    }
}