package app.personalfinance.ui.expenses;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.personalfinance.R;
import app.personalfinance.data.transactions.TransactionWithDetails;
import app.personalfinance.databinding.FragmentExpensesBinding;
import app.personalfinance.viewModel.expenses.ExpensesViewModel;
import app.personalfinance.viewModel.transactions.TransactionsViewModel;

// Fragment for the expenses list
public class ExpensesFragment extends Fragment {
    // Binding
    private FragmentExpensesBinding binding;
    // ViewModel
    private ExpensesViewModel expensesViewModel;

    private TransactionsViewModel transactionsViewModel;
    // RecyclerView
    private RecyclerView recyclerView;

    ImageView noDataImage;

    // Executor service to run the queries in background
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        expensesViewModel = new ViewModelProvider(this).get(ExpensesViewModel.class);

        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentExpensesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        noDataImage = binding.imageViewExpensesNoData;

        // get the button to add an expense
        Button addButton = binding.buttonAddExpenses;
        // Onclick
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_expensesFragment_to_transactionsFragment);
                // Move to the formExpensesFragment
            }
        });

        // RecyclerView
        recyclerView = binding.recyclerViewExpenses;
        // Set the layout manager

        // Bind the RecyclerView
        bindRecyclerView();

        return root;
    }

    void bindRecyclerView() {
        executor.execute(() -> {
            // Get the expenses list
            LiveData<List<TransactionWithDetails>> liveData = transactionsViewModel.getAllExpenses();

            // Observe the LiveData
            mainHandler.post(() -> liveData.observe(getViewLifecycleOwner(), expenseList -> {
                if (expenseList == null || expenseList.isEmpty()) {
                    noDataImage.setVisibility(View.VISIBLE);
                    Log.d("ExpensesFragment", "Expense list is null");
                } else {
                    noDataImage.setVisibility(View.GONE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    // Set the adapter
                    ExpenseAdapter adapter = new ExpenseAdapter(getContext(), (ArrayList<TransactionWithDetails>) expenseList, transactionsViewModel);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Log.d("ExpensesFragment", "Adapter updated with expenses size: " + adapter.getItemCount());

                    // Swipe to delete
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteExpenseCallback(adapter));
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                }
            }));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}