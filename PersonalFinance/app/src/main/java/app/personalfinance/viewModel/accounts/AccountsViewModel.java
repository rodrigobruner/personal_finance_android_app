package app.personalfinance.viewModel.accounts;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.function.Consumer;

import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.repo.accounts.AccountRepository;

public class AccountsViewModel extends AndroidViewModel {

    private AccountRepository repository;

    public AccountsViewModel(@NonNull Application application) {
        super(application);
        if(repository == null) {
            repository = new AccountRepository(application);
        }
    }


    public void saveAccount(String name, double balance, Consumer<Boolean> callback) {
        repository.insert(new AccountModel(name, balance, balance), callback);
    }

    public void updateAccountBalance(int accountID, double amount, Consumer<Boolean> callback) {
        repository.updateAccountBalance(accountID, amount, callback);
    }

    public void deleteAccount(AccountModel account, Consumer<Boolean> callback) {
        repository.delete(account, callback);
    }

    public LiveData<List<AccountModel>> getAllAccounts() {
        LiveData<List<AccountModel>> data = repository.getAllAccounts();
        return data;
    }
}