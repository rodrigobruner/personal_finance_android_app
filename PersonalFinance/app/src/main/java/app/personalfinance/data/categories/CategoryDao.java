package app.personalfinance.data.categories;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CategoryDao {
    @Insert
    void insert(CategoryModel category);

    @Query("SELECT * FROM categories ORDER BY id DESC")
    List<CategoryModel> getAllCategories();
}