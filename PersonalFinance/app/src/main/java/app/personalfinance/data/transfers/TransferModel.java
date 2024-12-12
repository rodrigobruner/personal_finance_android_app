package app.personalfinance.data.transfers;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.data.categories.CategoryModel;


//https://medium.com/@jaclync/android-room-with-nested-relationships-803dad19a500
@Entity(tableName = "transfers",
        foreignKeys = {
                @ForeignKey(entity = AccountModel.class,
                        parentColumns = "id",
                        childColumns = "fromAccount",
                        onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = AccountModel.class,
                        parentColumns = "id",
                        childColumns = "toAccount",
                        onDelete = ForeignKey.CASCADE),
        }) //Table name
public class TransferModel {
    //PK id
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int fromAccount;
    private int toAccount;
    private double amount;
    private String date;
    private String note;

    //Constructor
    public TransferModel(int fromAccount, int toAccount, double amount, String date, String note) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(int fromAccount) {
        this.fromAccount = fromAccount;
    }

    public int getToAccount() {
        return toAccount;
    }

    public void setToAccount(int toAccount) {
        this.toAccount = toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
