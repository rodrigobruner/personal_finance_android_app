package app.personalfinance.data.categories;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

// Data Access Object for CategoryModel
@Dao
public interface CategoryDao {
    @Insert
    void insert(CategoryModel category); // Insert a new category

    @Delete
    void delete(CategoryModel category); // Delete a category

    @Update
    void update(CategoryModel category); // Update a category

    @Query("SELECT * FROM categories ORDER BY type")
    LiveData<List<CategoryModel>> getAllCategories(); // Get all categories

    @Query("SELECT * FROM categories WHERE type = :type ORDER BY name")
    LiveData<List<CategoryModel>> getCategoriesByType(String type); // Get all categories by type
}