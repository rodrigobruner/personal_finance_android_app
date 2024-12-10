package app.personalfinance.ui.accounts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.personalfinance.R;
import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.helpper.CurrencyFormatter;
import app.personalfinance.viewModel.accounts.AccountsViewModel;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountViewHolder> {
    public ArrayList<AccountModel> accounts;
    private Context context;
    private AccountsViewModel accountsViewModel;


    public AccountAdapter(Context context,
                          ArrayList<AccountModel> accounts,
                          AccountsViewModel accountsViewModel) {
        this.context = context;
        this.accounts = accounts;
        this.accountsViewModel = accountsViewModel;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_accounts, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        AccountModel account = accounts.get(position);
        holder.accountName.setText(account.getName());
        holder.accountCurrentBalance.setText(CurrencyFormatter.format(account.getCurrentBalance()));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public void deleteItem(int position) {
        try {
            AccountModel account = accounts.get(position);
            accountsViewModel.deleteAccount(account);
            accounts.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, R.string.account_delete, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, R.string.account_delete_error, Toast.LENGTH_SHORT).show();
        }
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {
        TextView accountName, accountCurrentBalance;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            accountName = itemView.findViewById(R.id.textViewAccountName);
            accountCurrentBalance = itemView.findViewById(R.id.textViewAccountCurrentBalance);
        }
    }
}