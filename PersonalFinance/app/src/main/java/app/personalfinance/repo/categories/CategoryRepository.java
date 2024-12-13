package app.personalfinance.repo.categories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import app.personalfinance.data.PersonalFinanceDatabase;
import app.personalfinance.data.categories.CategoryDao;
import app.personalfinance.data.categories.CategoryModel;

public class CategoryRepository {
    // Dao instance
    private CategoryDao categoryDao;


    // Number of threads in the pool
    private static final int NUMBER_OF_THREADS = 2;
    //Flag to check if there is an error
    private final ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);


    // Constructor
    public CategoryRepository(Application application) {
        PersonalFinanceDatabase database = PersonalFinanceDatabase.getInstance(application);
        // Set dao
        categoryDao = database.categoryDao();
    }

    // Function to insert a category
    public void insert(CategoryModel category, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                categoryDao.insert(category);
                mainHandler.post(() -> {
                    callback.accept(true);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    callback.accept(false);
                });
                e.printStackTrace();
            }
        });
    }

    // Function to update a category
    public void update(CategoryModel category, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                categoryDao.update(category);
                mainHandler.post(() -> {
                    callback.accept(true);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    callback.accept(false);
                });
                e.printStackTrace();
            }
        });
    }

    // Function to delete a category
    public void delete(CategoryModel category, Consumer<Boolean> callback) {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                categoryDao.delete(category);
                mainHandler.post(() -> {
                    callback.accept(true);
                });
            } catch (Exception e) {
                mainHandler.post(() -> {
                    callback.accept(false);
                });
                e.printStackTrace();
            }
        });
    }

    // Function to get all categories
    public LiveData<List<CategoryModel>> getAllCategories() {
        try {
            return categoryDao.getAllCategories();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Function to get all categories by type
    public LiveData<List<CategoryModel>> getCategoriesByType(String type) {
        try {
            return categoryDao.getCategoriesByType(type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}