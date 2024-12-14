package app.personalfinance.data.transfers;

import androidx.room.Embedded;
import androidx.room.Relation;
import app.personalfinance.data.accounts.AccountModel;

// https://medium.com/@jaclync/android-room-with-nested-relationships-803dad19a500
// TransferWithDetails class, contains the transaction and accounts releted to the transfer
public class TransferWithDetails {
    @Embedded
    public TransferModel transfer;

    @Relation(
            parentColumn = "fromAccount",
            entityColumn = "id"
    )
    public AccountModel from;

    @Relation(
            parentColumn = "toAccount",
            entityColumn = "id"
    )
    public AccountModel to;
}