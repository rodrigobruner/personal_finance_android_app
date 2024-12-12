package app.personalfinance.viewModel.accounts;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.repo.accounts.AccountRepository;

public class AccountsViewModel extends AndroidViewModel {

    private AccountRepository repository;

    public AccountsViewModel(@NonNull Application application) {
        super(application);
        repository = new AccountRepository(application);
    }

    public void saveAccount(String name, double balance) {
        repository.insert(new AccountModel(name, balance, balance));
    }

    public void updateAccountBalance(int accountID, double amount) {
        repository.updateAccountBalance(accountID, amount);
    }

    public void deleteAccount(AccountModel account) {
        repository.delete(account);
    }

    public LiveData<List<AccountModel>> getAllAccounts() {
        LiveData<List<AccountModel>> data = repository.getAllAccounts();
//        Log.d("AccountsViewModel", "getAllAccounts: " + data.get());
        return data;
    }
}