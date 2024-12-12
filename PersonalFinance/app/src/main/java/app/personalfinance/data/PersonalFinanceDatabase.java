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
import app.personalfinance.data.transactions.TransactionDAO;
import app.personalfinance.data.transactions.TransactionModel;
import app.personalfinance.data.transfers.TransferDao;
import app.personalfinance.data.transfers.TransferModel;

// Set up the database, define the entities and the version
@Database(entities = {CategoryModel.class, AccountModel.class, TransactionModel.class, TransferModel.class}, version = 4)
public abstract class PersonalFinanceDatabase extends RoomDatabase {

    private static volatile PersonalFinanceDatabase instance; // Singleton
    private static final int NUMBER_OF_THREADS = 4; // Number of threads
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS); // ExecutorService
    private static final String DATABASE_NAME = "personal_finance_database";

    // Define the DAOs
    public abstract CategoryDao categoryDao();
    public abstract AccountDao accountDao();
    public abstract TransactionDAO transactionDao();
    public abstract TransferDao transferDao();

    // Get the instance of the database
    public static PersonalFinanceDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (PersonalFinanceDatabase.class) {
                if (instance == null) {
                    // Create the database
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    PersonalFinanceDatabase.class, DATABASE_NAME)
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
            // Populate the database
            databaseWriteExecutor.execute(() -> {
                CategoryDao categoryDao = instance.categoryDao();
                AccountDao accountDao = instance.accountDao();
                TransactionDAO transactionDAO = instance.transactionDao();
                TransferDao transferDao = instance.transferDao();
            });
        }
    };
}