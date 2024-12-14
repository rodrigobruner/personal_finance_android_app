package app.personalfinance.ui.accounts;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import app.personalfinance.R;
import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.helpper.CurrencyFormatter;
import app.personalfinance.viewModel.accounts.AccountsViewModel;

// Adapter for the accounts list, used in the accounts fragment RecyclerView
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    // List of accounts
    public ArrayList<AccountModel> accounts;
    // Context
    private Context context;
    // ViewModel for delete accounts
    private AccountsViewModel accountsViewModel;

    // Executor and Handler
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    // Interface for item click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener listener;

    // Constructor
    public AccountAdapter(Context context,
                          ArrayList<AccountModel> accounts,
                          AccountsViewModel accountsViewModel) {
        this.context = context;
        this.accounts = accounts;
        this.accountsViewModel = accountsViewModel;
    }

    // Method to set the item click listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Describes an item view and metadata
    class ViewHolder extends RecyclerView.ViewHolder {

        // Define the widgets
        public TextView accountName, accountCurrentBalance;

        // Constructor
        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            // Get instances of the widgets
            accountName = itemView.findViewById(R.id.textViewAccountName);
            accountCurrentBalance = itemView.findViewById(R.id.textViewAccountCurrentBalance);

            // Set the click listener for the item view
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_accounts, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccountModel account = accounts.get(position);
        // Set the account name
        holder.accountName.setText(account.getName());
        // Set the account current balance
        holder.accountCurrentBalance.setText(CurrencyFormatter.format(account.getCurrentBalance()));
    }

    @Override
    public int getItemCount() {
        // Return the number of accounts
        return accounts.size();
    }

    // Function to delete an account
    public void deleteItem(int position) {
        try {
            AccountModel account = accounts.get(position); // Get the account
            accountsViewModel.deleteAccount(account, status -> {
                if (status) {
                    accounts.remove(position); // Remove the account from the list
                    notifyItemRemoved(position); // Notify the adapter that the item was removed
                    Toast.makeText(context, R.string.account_delete, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.account_delete_error, Toast.LENGTH_SHORT).show();
                }
                this.notifyDataSetChanged();
            });
        } catch (Exception e) {
            // Notify the user that the account cannot be deleted due to linked transactions
            Toast.makeText(context, R.string.account_delete_error, Toast.LENGTH_SHORT).show();
        }
    }
}