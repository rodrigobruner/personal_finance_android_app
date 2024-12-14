package app.personalfinance.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import app.personalfinance.R;
import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.helpper.CurrencyFormatter;

public class AccountsCardAdapter extends RecyclerView.Adapter<AccountsCardAdapter.ViewHolder> {
    // List of accounts
    private List<AccountModel> accounts;
    // Context
    private Context context;

    public AccountsCardAdapter(Context context, List<AccountModel> accounts) {
        this.context = context;
        this.accounts = accounts;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // define account name and balance
        TextView accountName, accountBalance;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // set widgets
            accountName = itemView.findViewById(R.id.accountCardName);
            accountBalance = itemView.findViewById(R.id.accountCardBalance);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_account, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the account
        AccountModel account = accounts.get(position);
        // Set the account name and balance
        holder.accountName.setText(account.getName());
        holder.accountBalance.setText(CurrencyFormatter.format(account.getCurrentBalance()));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }
}