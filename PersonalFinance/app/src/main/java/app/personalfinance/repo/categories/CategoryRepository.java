package app.personalfinance.repo.categories;

import android.app.Application;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.personalfinance.data.categories.CategoryDao;
import app.personalfinance.data.categories.CategoryDatabase;
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
        CategoryDatabase database = CategoryDatabase.getInstance(application);
        // Set dao
        categoryDao = database.categoryDao();

        // Set executor service
        dbExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }

    // Function to insert category
    public void insert(CategoryModel model) {
        // Run in background the insert command
        dbExecutor.execute(() -> categoryDao.insert(model));
    }

    // Function to get all categories
    public List<CategoryModel> getAllCategories() {
        return categoryDao.getAllCategories();
    }
}