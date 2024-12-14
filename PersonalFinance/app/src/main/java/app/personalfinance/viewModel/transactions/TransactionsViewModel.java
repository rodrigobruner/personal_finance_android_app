package app.personalfinance.viewModel.transactions;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.function.Consumer;
import app.personalfinance.data.helpper.DataChartLabelValueModel;
import app.personalfinance.data.transactions.TransactionModel;
import app.personalfinance.data.transactions.TransactionWithDetails;
import app.personalfinance.repo.transactions.TransactionRepository;

public class TransactionsViewModel extends AndroidViewModel {
    //repository
    private TransactionRepository repository;
    //constructor
    public TransactionsViewModel(@NonNull Application application) {
        super(application);
        //initialize repository
        repository = new TransactionRepository(application);
    }
    //insert transaction
    public void insertTransaction(int account, int category, double amount, String description, String date, String type, Consumer<Boolean> callback) {
        repository.insert(new TransactionModel(account, category, amount, description, date, type), callback);
    }
    //delete transaction
    public void deleteTransaction(TransactionModel transaction, Consumer<Boolean> callback) {
        repository.delete(transaction, callback);
    }
    //get all transactions
    public LiveData<List<TransactionWithDetails>> getAllTransactions() {
        return repository.getAllTransactions();
    }
    //get all incomes
    public LiveData<List<TransactionWithDetails>> getAllIncomes() {
        return repository.getAllIncomes();
    }
    //get all expenses
    public LiveData<List<TransactionWithDetails>> getAllExpenses() {
        return repository.getAllExpenses();
    }
    //get all transactions by type
    public LiveData<List<DataChartLabelValueModel>> getCurrentMonthSummaryByType(String type) {
        return repository.getCurrentMonthSummaryByType(type);
    }
}