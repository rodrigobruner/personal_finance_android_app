package app.personalfinance.data.accounts;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

// Data Access Object for AccountModel
@Dao
public interface AccountDao {
    @Insert
    void insert(AccountModel account);

    @Delete
    void delete(AccountModel account);

    @Query("SELECT * FROM accounts ORDER BY id DESC LIMIT 1")
    AccountModel getLast();

    @Query("SELECT * FROM accounts ORDER BY name")
    LiveData<List<AccountModel>> getAllAccounts();
}