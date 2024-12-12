package app.personalfinance.ui.incomes;

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
import app.personalfinance.helpper.CurrencyFormatter;
import app.personalfinance.viewModel.transactions.TransactionsViewModel;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {
    private ArrayList<TransactionWithDetails> incomes;
    private Context context;
    TransactionsViewModel transactionsViewModel;

    public IncomeAdapter(Context context, ArrayList<TransactionWithDetails> incomes, TransactionsViewModel transactionsViewModel) {
        this.context = context;
        this.incomes = incomes;
        this.transactionsViewModel = transactionsViewModel;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView categoryAccount, date, amount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.imageViewIcon);
            categoryAccount = itemView.findViewById(R.id.textViewCategoryAccount);
            date = itemView.findViewById(R.id.textViewDate);
            amount = itemView.findViewById(R.id.textViewIncomeAmount);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_income, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionWithDetails income = incomes.get(position);
        holder.icon.setImageResource(R.drawable.icon_income); // Set the icon resource
        holder.categoryAccount.setText(income.category.getName() + " -> " + income.account.getName());
        holder.date.setText(income.transaction.getDate());
        holder.amount.setText(CurrencyFormatter.format(income.transaction.getAmount()));
    }

    public void deleteItem(int position) {
        if (position >= 0 && position < incomes.size()) {
            try {
                TransactionWithDetails income = incomes.get(position); // Get the income
                incomes.remove(position); // Remove the income from the list
                transactionsViewModel.deleteTransaction(income.transaction); // Delete the income
                notifyItemRemoved(position); // Notify the adapter that the item was removed
                // Notify the user that the income was deleted
                Toast.makeText(context, R.string.income_delete, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Notify the user that the income was not deleted
                Toast.makeText(context, R.string.income_delete_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.e("IncomeAdapter", "Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }
}