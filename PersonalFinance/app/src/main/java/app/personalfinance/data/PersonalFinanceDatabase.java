package app.personalfinance.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.personalfinance.data.accounts.AccountDao;
import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.data.categories.CategoryDao;
import app.personalfinance.data.categories.CategoryModel;

@Database(entities = {CategoryModel.class, AccountModel.class}, version = 1)
public abstract class PersonalFinanceDatabase extends RoomDatabase {
    private static volatile PersonalFinanceDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract CategoryDao categoryDao();
    public abstract AccountDao accountDao();

    public static PersonalFinanceDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (PersonalFinanceDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    PersonalFinanceDatabase.class, "personal_finance_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                CategoryDao categoryDao = instance.categoryDao();
                AccountDao accountDao = instance.accountDao();
            });
        }
    };
}