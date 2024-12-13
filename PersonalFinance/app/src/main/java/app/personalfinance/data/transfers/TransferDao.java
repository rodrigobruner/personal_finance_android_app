package app.personalfinance.data.transfers;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// Data Access object for TransferModel
@Dao
public interface TransferDao {
    @Insert
    void insert(TransferModel transfer);

    @Update
    void update(TransferModel transfer);

    @Delete
    void delete(TransferModel transfer);

    @Query("SELECT * FROM transfers ORDER BY id DESC")
    LiveData<List<TransferWithDetails>> getAllTransfers();
}