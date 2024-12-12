package app.personalfinance.data.transactions;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import app.personalfinance.data.categories.CategoriesTypes;

// Data Access Object for transactionModel
@Dao
public interface TransactionDAO {
    @Insert
    void insert(TransactionModel transaction);

    @Delete
    void delete(TransactionModel transaction);

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    LiveData<List<TransactionWithDetails>> getAllTransactions();

    @Query("SELECT * FROM transactions WHERE type = 'Income' ORDER BY date DESC")
    LiveData<List<TransactionWithDetails>> getAllIncomes();

    @Query("SELECT * FROM transactions WHERE type = '"+ CategoriesTypes.EXPENSE+"' ORDER BY date DESC")
    LiveData<List<TransactionWithDetails>> getAllExpenses();
}
