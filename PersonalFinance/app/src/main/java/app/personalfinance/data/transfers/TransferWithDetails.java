package app.personalfinance.data.transfers;

import androidx.room.Embedded;
import androidx.room.Relation;
import app.personalfinance.data.accounts.AccountModel;

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