package app.personalfinance.data.accounts;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity(tableName = "accounts") // Table name
public class AccountModel implements Serializable {
    // Primary key
    @PrimaryKey(autoGenerate = true)
    private int id; // Account ID
    private String name; // Account name
    private double balance; // Initial account balance
    private double currentBalance; // Current account balance

    // Constructor
    public AccountModel(int id, String name, double balance, double currentBalance) {
        this.id = id;
        this.name = name;
        this.balance = balance;
        this.currentBalance = currentBalance;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
}