package app.personalfinance.ui.transfers;

import android.content.Context;
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
import app.personalfinance.data.transfers.TransferWithDetails;
import app.personalfinance.helpper.CurrencyFormatter;
import app.personalfinance.viewModel.transfers.TransfersViewModel;

public class TransferAdapter extends RecyclerView.Adapter<TransferAdapter.ViewHolder> {
    // List of transfers
    private ArrayList<TransferWithDetails> transfers;
    // Context
    private Context context;
    // TransfersViewModel
    TransfersViewModel transfersViewModel;

    public TransferAdapter(Context context, ArrayList<TransferWithDetails> transfers, TransfersViewModel transfersViewModel) {
        this.context = context;
        this.transfers = transfers;
        this.transfersViewModel = transfersViewModel;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // Widgets
        ImageView icon;
        TextView categoryAccount, date, amount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get the widgets
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
        // Get the transfer
        TransferWithDetails transfer = transfers.get(position);
        // Set the values
        holder.icon.setImageResource(R.drawable.icon_transfers); // Set the icon resource
        holder.categoryAccount.setText(transfer.from.getName() + " -> " + transfer.to.getName());
        holder.date.setText(transfer.transfer.getDate());
        holder.amount.setText(CurrencyFormatter.format(transfer.transfer.getAmount()));
    }

    public void deleteItem(int position) {
        try {
            TransferWithDetails transfer = transfers.get(position); // Get the transfer
            transfersViewModel.deleteTransfer(transfer.transfer, status -> {
                if(status) { // If the transfer was deleted
                    transfers.remove(position); // Remove the transfer from the list
                    notifyItemRemoved(position); // Notify the adapter that the item was removed
                    // Notify the user that the transfer was deleted
                    Toast.makeText(context, R.string.transfer_delete, Toast.LENGTH_SHORT).show();
                } else {
                    this.notifyDataSetChanged();
                    // Notify the user that the transfer was not deleted
                    Toast.makeText(context, R.string.transfer_delete_error, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            // Notify the user that the transfer was not deleted
            Toast.makeText(context, R.string.transfer_delete_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return transfers.size();
    }
}