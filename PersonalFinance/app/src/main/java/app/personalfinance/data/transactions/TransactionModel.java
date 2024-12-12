package app.personalfinance.data.transactions;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.data.categories.CategoryModel;

//https://medium.com/@jaclync/android-room-with-nested-relationships-803dad19a500
@Entity(tableName = "transactions",
        foreignKeys = {
                @ForeignKey(entity = AccountModel.class,
                        parentColumns = "id",
                        childColumns = "accountId",
                        onDelete = ForeignKey.RESTRICT),
                @ForeignKey(entity = CategoryModel.class,
                        parentColumns = "id",
                        childColumns = "categoryId",
                        onDelete = ForeignKey.RESTRICT)
        }) //Table name
public class TransactionModel {

    //PK id
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int accountId;
    private int categoryId;
    private double amount;
    private String description;
    private String date;
    private String type;

    //Constructor
    public TransactionModel(int accountId, int categoryId, double amount, String description, String date, String type) {
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
    }


    //Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
