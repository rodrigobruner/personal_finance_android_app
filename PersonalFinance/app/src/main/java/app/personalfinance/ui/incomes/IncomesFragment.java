package app.personalfinance.ui.incomes;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.personalfinance.MainActivity;
import app.personalfinance.R;
import app.personalfinance.data.transactions.TransactionWithDetails;
import app.personalfinance.databinding.FragmentIncomesBinding;
import app.personalfinance.viewModel.transactions.TransactionsViewModel;

// Fragment for the incomes list
public class IncomesFragment extends Fragment {
    // Binding
    private FragmentIncomesBinding binding;

    private TransactionsViewModel transactionsViewModel;
    // RecyclerView
    private RecyclerView recyclerView;

    ImageView noDataImage;

    // Executor service to run the queries in background
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentIncomesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get the button to add an income
        Button addButton = binding.buttonAddIncomes;
        noDataImage = binding.imageViewIncomesNoData;

        // Onclick
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_incomesFragment_to_transactionsFragment);
                // Move to the formIncomesFragment
            }
        });

        // RecyclerView
        recyclerView = binding.recyclerViewIncomes;
        // Set the layout manager

        // Bind the RecyclerView
        bindRecyclerView();

        return root;
    }

    void bindRecyclerView() {
        executor.execute(() -> {
            // Get the incomes list
            LiveData<List<TransactionWithDetails>> liveData = transactionsViewModel.getAllIncomes();

            // Observe the LiveData
            mainHandler.post(() -> liveData.observe(getViewLifecycleOwner(), incomeList -> {
                if (incomeList == null || incomeList.isEmpty()) {
                    noDataImage.setVisibility(View.VISIBLE); // Show the no data image
                } else {
                    noDataImage.setVisibility(View.GONE); // Hide the no data image
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    // create the adapter
                    IncomeAdapter adapter = new IncomeAdapter(getContext(), (ArrayList<TransactionWithDetails>) incomeList, transactionsViewModel);
                    recyclerView.setAdapter(adapter); // Set the adapter
                    adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
                    // Swipe to delete
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteIncomeCallback(adapter));
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