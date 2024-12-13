package app.personalfinance.viewModel.transactions;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import app.personalfinance.data.helpper.DataChartLabelValue;
import app.personalfinance.data.transactions.TransactionModel;
import app.personalfinance.data.transactions.TransactionWithDetails;
import app.personalfinance.repo.transactions.TransactionRepository;

public class TransactionsViewModel extends AndroidViewModel {

    private TransactionRepository repository;

    public TransactionsViewModel(@NonNull Application application) {
        super(application);
        repository = new TransactionRepository(application);
    }

    public void insertTransaction(int account, int category, double amount, String description, String date, String type) {
        repository.insert(new TransactionModel(account, category, amount, description, date, type));
    }

    public void deleteTransaction(TransactionModel transaction) {
        repository.delete(transaction);
    }

    public LiveData<List<TransactionWithDetails>> getAllTransactions() {
        return repository.getAllTransactions();
    }

    public LiveData<List<TransactionWithDetails>> getAllIncomes() {
        return repository.getAllIncomes();
    }

    public LiveData<List<TransactionWithDetails>> getAllExpenses() {
        return repository.getAllExpenses();
    }

    public LiveData<List<DataChartLabelValue>> getCurrentMonthSummaryByType(String type) {
        return repository.getCurrentMonthSummaryByType(type);
    }
}