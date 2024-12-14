package app.personalfinance.ui.incomes;

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
import app.personalfinance.data.transactions.TransactionWithDetails;
import app.personalfinance.helpper.CurrencyFormatter;
import app.personalfinance.viewModel.transactions.TransactionsViewModel;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {
    // List of incomes
    private ArrayList<TransactionWithDetails> incomes;
    // Context
    private Context context;
    // TransactionsViewModel
    TransactionsViewModel transactionsViewModel;

    // Constructor
    public IncomeAdapter(Context context, ArrayList<TransactionWithDetails> incomes, TransactionsViewModel transactionsViewModel) {
        this.context = context;
        this.incomes = incomes;
        this.transactionsViewModel = transactionsViewModel;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        // define icon, category and account, date and amount
        ImageView icon;
        TextView categoryAccount, date, amount;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            // set widgets
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
        // Get the income
        TransactionWithDetails income = incomes.get(position);
        // Set the category, account, date and amount
        holder.icon.setImageResource(R.drawable.icon_income); // Set the icon resource
        holder.categoryAccount.setText(income.category.getName() + " -> " + income.account.getName());
        holder.date.setText(income.transaction.getDate());
        holder.amount.setText(CurrencyFormatter.format(income.transaction.getAmount()));
    }

    public void deleteItem(int position) {
        try {
            TransactionWithDetails income = incomes.get(position); // Get the income
            transactionsViewModel.deleteTransaction(income.transaction, status ->{
                if(status) {
                    incomes.remove(position); // Remove the income from the list
                    notifyItemRemoved(position); // Notify the adapter that the item was removed
                    // Notify the user that the income was deleted
                    Toast.makeText(context, R.string.income_delete, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.income_delete_error, Toast.LENGTH_SHORT).show();
                }
                this.notifyDataSetChanged();
            }); // Delete the income
        } catch (Exception e) {
            // Notify the user that the income was not deleted
            Toast.makeText(context, R.string.income_delete_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }
}