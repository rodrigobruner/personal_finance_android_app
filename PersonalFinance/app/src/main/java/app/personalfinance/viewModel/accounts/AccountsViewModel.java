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

    // Repository
    private AccountRepository repository;

    // Constructor
    public AccountsViewModel(@NonNull Application application) {
        super(application);
        //set the repository
        repository = new AccountRepository(application);
    }

    // save account
    public void saveAccount(int id, String name, double balance, Consumer<Boolean> callback) {
        repository.insert(new AccountModel(0, name, balance, balance), callback);
    }

    // update account
    public void updateAccountBalance(int accountID, double amount, Consumer<Boolean> callback) {
        repository.updateAccountBalance(accountID, amount, callback);
    }

    // delete account
    public void deleteAccount(AccountModel account, Consumer<Boolean> callback) {
        repository.delete(account, callback);
    }

    // get account
    public AccountModel getAccount(int id) {
        return repository.getAccount(id);
    }

    // get all accounts
    public LiveData<List<AccountModel>> getAllAccounts() {
        LiveData<List<AccountModel>> data = repository.getAllAccounts();
        return data;
    }
}