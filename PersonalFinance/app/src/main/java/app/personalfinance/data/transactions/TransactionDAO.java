package app.personalfinance.data.transactions;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import app.personalfinance.data.categories.CategoriesTypes;
import app.personalfinance.data.helpper.DataChartLabelValue;

// Data Access Object for transactionModel
@Dao
public interface TransactionDAO {
    @Insert
    void insert(TransactionModel transaction);

    @Update
    void update(TransactionModel transaction);

    @Delete
    void delete(TransactionModel transaction);

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    LiveData<List<TransactionWithDetails>> getAllTransactions();

    @Query("SELECT * FROM transactions WHERE type = '"
            + CategoriesTypes.INCOME+
            "' ORDER BY date DESC")
    LiveData<List<TransactionWithDetails>> getAllIncomes();

    @Query("SELECT * FROM transactions WHERE type = '"
            + CategoriesTypes.EXPENSE +
            "' ORDER BY date DESC")
    LiveData<List<TransactionWithDetails>> getAllExpenses();

    @Query("SELECT c.name AS label, SUM(t.amount) AS value FROM transactions AS t " +
            "INNER JOIN categories AS c ON t.categoryId = c.id " +
            "WHERE t.type = :type AND strftime('%Y-%m', t.date) = strftime('%Y-%m', 'now') " +
            "GROUP BY t.categoryId")
    LiveData<List<DataChartLabelValue>> getCurrentMonthSummaryByType(String type);

}
