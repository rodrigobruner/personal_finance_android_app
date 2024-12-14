package app.personalfinance.data.transactions;

import androidx.room.Embedded;
import androidx.room.Relation;
import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.data.categories.CategoryModel;

// https://medium.com/@jaclync/android-room-with-nested-relationships-803dad19a500
// TransactionWithDetails class, contains the transaction and accounts and categories releted to the transaction
public class TransactionWithDetails {
    @Embedded
    public TransactionModel transaction;

    @Relation(
            parentColumn = "accountId",
            entityColumn = "id"
    )
    public AccountModel account;

    @Relation(
            parentColumn = "categoryId",
            entityColumn = "id"
    )
    public CategoryModel category;
}