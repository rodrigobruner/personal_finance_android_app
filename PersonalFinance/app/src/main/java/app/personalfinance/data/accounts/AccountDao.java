package app.personalfinance.data.accounts;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

// Data Access Object for AccountModel
@Dao
public interface AccountDao {
    @Insert
    void insert(AccountModel account); // Insert a new account

    @Update
    void update(AccountModel account); // Update an existing account

    @Delete
    void delete(AccountModel account); // Delete an account

    @Query("UPDATE accounts SET currentBalance = currentBalance + :amount WHERE id = :accountID")
    void updateAccountBalance(int accountID, double amount); // Update account balance

    @Query("SELECT * FROM accounts ORDER BY id DESC LIMIT 1")
    AccountModel getLast(); // Get the last account

    @Query("SELECT * FROM accounts WHERE id = :id")
    AccountModel getAccount(int id); // Get an account by ID

    @Query("SELECT * FROM accounts ORDER BY name")
    LiveData<List<AccountModel>> getAllAccounts(); // Get all accounts
}