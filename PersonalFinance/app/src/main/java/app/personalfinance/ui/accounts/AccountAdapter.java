package app.personalfinance.ui.accounts;

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
import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.helpper.CurrencyFormatter;
import app.personalfinance.viewModel.accounts.AccountsViewModel;

// Adapter for the accounts list, used in the accounts fragment RecyclerView
public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.ViewHolder> {
    // List of accounts
    public ArrayList<AccountModel> accounts;
    // Context,
    private Context context;
    // ViewModel for delete accounts
    private AccountsViewModel accountsViewModel; // ViewModel

    // Constructor
    public AccountAdapter(Context context,
                          ArrayList<AccountModel> accounts,
                          AccountsViewModel accountsViewModel) {
        this.context = context;
        this.accounts = accounts;
        this.accountsViewModel = accountsViewModel;
    }

    // Describes an item view and metadata
    class ViewHolder extends RecyclerView.ViewHolder {

        //Define the widgets
        public TextView accountName, accountCurrentBalance;

        //Constructor
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Get instances of the widgets
            accountName = itemView.findViewById(R.id.textViewAccountName);
            accountCurrentBalance = itemView.findViewById(R.id.textViewAccountCurrentBalance);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_accounts, parent, false);
        return new ViewHolder(view);
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
            accountsViewModel.deleteAccount(account); // Delete the account
            accounts.remove(position); // Remove the account from the list
            notifyItemRemoved(position); // Notify the adapter that the item was removed
            // Notify the user that the account was deleted
            Toast.makeText(context, R.string.account_delete, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Notify the user that the account was not deleted
            Toast.makeText(context, R.string.account_delete_error, Toast.LENGTH_SHORT).show();
        }
    }
}