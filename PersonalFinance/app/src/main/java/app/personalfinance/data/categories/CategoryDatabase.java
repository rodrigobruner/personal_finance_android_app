package app.personalfinance.data.categories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.personalfinance.data.categories.CategoryDao;
import app.personalfinance.data.categories.CategoryModel;

// This annotation defines a Room database with CategoryModel in version 1
@Database(entities = {CategoryModel.class}, version = 1)
public abstract class CategoryDatabase extends RoomDatabase {
    // A singleton instance of the database
    private static volatile CategoryDatabase instance;
    // Number of threads for the service
    private static final int NUMBER_OF_THREADS = 4;
    // Create an Executor Service to run the db activities in another thread (background), not in the main thread
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    // Get DAO
    public abstract CategoryDao categoryDao();

    // Get the singleton instance
    public static CategoryDatabase getInstance(final Context context) {
        if (instance == null) { // If no instance, create one
            synchronized (CategoryDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    CategoryDatabase.class, "category_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback) // Callback to populate DB when it is created
                            .build();
                }
            }
        }
        return instance;
    }

    // Callback to populate DB when it is created
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Execute db operations in background
            databaseWriteExecutor.execute(() -> {
                CategoryDao dao = instance.categoryDao();
                // You can add initial data here if needed
            });
        }
    };
}