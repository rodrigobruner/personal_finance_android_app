package app.personalfinance.repo.categories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.personalfinance.data.PersonalFinanceDatabase;
import app.personalfinance.data.categories.CategoryDao;
import app.personalfinance.data.categories.CategoryModel;

public class CategoryRepository {
    // Dao instance
    private CategoryDao categoryDao;
    // Number of threads in the pool
    private static final int NUMBER_OF_THREADS = 2;
    // Instance to execute the queries in background
    private final ExecutorService dbExecutor;

    // Constructor
    public CategoryRepository(Application application) {
        PersonalFinanceDatabase database = PersonalFinanceDatabase.getInstance(application);
        // Set dao
        categoryDao = database.categoryDao();

        // Set executor service
        dbExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }

    // Function to insert a category
    public void insert(CategoryModel model) {
        // Run in background the insert command
        dbExecutor.execute(() -> categoryDao.insert(model));
    }

    // Function to delete a category
    public void delete(CategoryModel model) {
        dbExecutor.execute(() ->
                categoryDao.delete(model));
    }

    // Function to get all categories
    public LiveData<List<CategoryModel>> getAllCategories() {
        return categoryDao.getAllCategories();
    }

    // Function to get all categories by type
    public LiveData<List<CategoryModel>> getCategoriesByType(String type) {
        return categoryDao.getCategoriesByType(type);
    }
}