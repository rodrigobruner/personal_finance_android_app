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
    void insert(TransferModel transfer); // Insert a new transfer

    @Update
    void update(TransferModel transfer); // Update a transfer

    @Delete
    void delete(TransferModel transfer); // Delete a transfer

    @Query("SELECT * FROM transfers ORDER BY id DESC")
    LiveData<List<TransferWithDetails>> getAllTransfers(); // Get all transfers
}