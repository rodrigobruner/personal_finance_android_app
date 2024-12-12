package app.personalfinance.data.transactions;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.data.categories.CategoryModel;

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