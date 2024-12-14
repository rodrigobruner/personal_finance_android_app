package app.personalfinance.data.transactions;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import app.personalfinance.data.categories.CategoriesTypes;
import app.personalfinance.data.helpper.DataChartLabelValueModel;

// Data Access Object for transactionModel
@Dao
public interface TransactionDAO {
    @Insert
    void insert(TransactionModel transaction); // Insert a new transaction

    @Update
    void update(TransactionModel transaction); // Update a transaction

    @Delete
    void delete(TransactionModel transaction); // Delete a transaction

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    LiveData<List<TransactionWithDetails>> getAllTransactions(); // Get all transactions

    @Query("SELECT * FROM transactions WHERE type = '"
            + CategoriesTypes.INCOME+
            "' ORDER BY date DESC")
    LiveData<List<TransactionWithDetails>> getAllIncomes(); // Get all incomes

    @Query("SELECT * FROM transactions WHERE type = '"
            + CategoriesTypes.EXPENSE +
            "' ORDER BY date DESC")
    LiveData<List<TransactionWithDetails>> getAllExpenses(); // Get all expenses

    @Query("SELECT c.name AS label, SUM(t.amount) AS value FROM transactions AS t " +
            "INNER JOIN categories AS c ON t.categoryId = c.id " +
            "WHERE t.type = :type AND strftime('%Y-%m', t.date) = strftime('%Y-%m', 'now') GROUP BY t.categoryId")
    LiveData<List<DataChartLabelValueModel>> getCurrentMonthSummaryByType(String type); // Get the current month summary by type

}
