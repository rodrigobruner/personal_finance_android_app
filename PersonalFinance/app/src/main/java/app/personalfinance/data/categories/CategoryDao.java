package app.personalfinance.data.categories;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import app.personalfinance.data.accounts.AccountModel;

@Dao
public interface CategoryDao {
    @Insert
    void insert(CategoryModel category);

    @Delete
    void delete(CategoryModel category);

    @Query("SELECT * FROM categories ORDER BY type")
    LiveData<List<CategoryModel>> getAllCategories();

    @Query("SELECT * FROM categories WHERE type = :type ORDER BY name")
    LiveData<List<CategoryModel>> getCategoriesByType(String type);
}