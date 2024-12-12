package app.personalfinance.ui.transfers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import app.personalfinance.R;
import app.personalfinance.data.transactions.TransactionWithDetails;
import app.personalfinance.data.transfers.TransferWithDetails;
import app.personalfinance.helpper.CurrencyFormatter;
import app.personalfinance.viewModel.transactions.TransactionsViewModel;
import app.personalfinance.viewModel.transfers.TransfersViewModel;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolder> {
    private ArrayList<TransferWithDetails> transfers;
    private Context context;
    TransfersViewModel transfersViewModel;

    public TransferAdapter(Context context, ArrayList<TransferWithDetails> transfers, TransfersViewModel transfersViewModel) {
        this.context = context;
        this.transfers = transfers;
        this.transfersViewModel = transfersViewModel;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView categoryAccount, date, amount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.imageViewIcon);
            categoryAccount = itemView.findViewById(R.id.textViewAccounts);
            date = itemView.findViewById(R.id.textViewDate);
            amount = itemView.findViewById(R.id.textViewTransferAmount);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_transfer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransferWithDetails transfer = transfers.get(position);
        holder.icon.setImageResource(R.drawable.icon_transfers); // Set the icon resource
        holder.categoryAccount.setText(transfer.from.getName() + " -> " + transfer.to.getName());
        holder.date.setText(transfer.transfer.getDate());
        holder.amount.setText(CurrencyFormatter.format(transfer.transfer.getAmount()));
    }

    public void deleteItem(int position) {
        if (position >= 0 && position < transfers.size()) {
            try {
                TransferWithDetails transfer = transfers.get(position); // Get the transfer
                transfers.remove(position); // Remove the transfer from the list
                transfersViewModel.deleteTransfer(transfer.transfer); // Delete the transfer from the database
                notifyItemRemoved(position); // Notify the adapter that the item was removed
                // Notify the user that the transfer was deleted
                Toast.makeText(context, R.string.transfer_delete, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Notify the user that the transfer was not deleted
                Toast.makeText(context, R.string.transfer_delete_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("TransferAdapter", "Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return transfers.size();
    }
}