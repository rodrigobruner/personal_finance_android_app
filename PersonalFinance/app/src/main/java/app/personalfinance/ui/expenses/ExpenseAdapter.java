package app.personalfinance.ui.expenses;

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

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {
    // List of expenses
    private ArrayList<TransactionWithDetails> expenses;
    // Context
    private Context context;
    // TransactionsViewModel
    TransactionsViewModel transactionsViewModel;

    // Constructor
    public ExpenseAdapter(Context context, ArrayList<TransactionWithDetails> expenses, TransactionsViewModel transactionsViewModel) {
        this.context = context;
        this.expenses = expenses;
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
        // Get the expense
        TransactionWithDetails expense = expenses.get(position);
        holder.icon.setImageResource(R.drawable.icon_expense); // Set the icon resource
        // Set the category, account, date and amount
        holder.categoryAccount.setText(expense.category.getName() + " -> " + expense.account.getName());
        holder.date.setText(expense.transaction.getDate());
        holder.amount.setText(CurrencyFormatter.format(expense.transaction.getAmount()));
    }

    public void deleteItem(int position) {
        try {
            TransactionWithDetails expense = expenses.get(position); // Get the expense
            transactionsViewModel.deleteTransaction(expense.transaction, status ->{
                if(status){
                    expenses.remove(position); // Remove the expense from the list
                    notifyItemRemoved(position); // Notify the adapter that the item was removed
                    // Notify the user that the expense was deleted
                    Toast.makeText(context, R.string.income_delete, Toast.LENGTH_SHORT).show();
                } else { // Notify the user that the expense was not deleted
                    Toast.makeText(context, R.string.income_delete_error, Toast.LENGTH_SHORT).show();
                }
                this.notifyDataSetChanged();
            }); // Delete the expense
        } catch (Exception e) {
            // Notify the user that the expense was not deleted
            Toast.makeText(context, R.string.income_delete_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}