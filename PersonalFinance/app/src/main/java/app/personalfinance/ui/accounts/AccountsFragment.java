package app.personalfinance.ui.accounts;

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
import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.databinding.FragmentAccountsBinding;
import app.personalfinance.viewModel.accounts.AccountsViewModel;

// Fragment for the accounts list
public class AccountsFragment extends Fragment {
    // Binding
    private FragmentAccountsBinding binding;
    // ViewModel
    private AccountsViewModel accountsViewModel;
    // RecyclerView
    private RecyclerView recyclerView;

    ImageView noDataImage;

    // Executor service to run the queries in background
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountsViewModel = new ViewModelProvider(this).get(AccountsViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentAccountsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        noDataImage = binding.imageViewAccountsNoData;

        // get the button to add an account
        Button addButton = binding.buttonAddAccount;

        //Onclick
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_accountsFragment_to_formAccountsFragment);
                //Move to the formAccountsFragment
            }
        });

        // RecyclerView
        recyclerView = binding.recyclerViewAccounts;
        // Set the layout manager

        // Bind the RecyclerView
        bindRecyclerView();



        return root;
    }

    void bindRecyclerView() {
        executor.execute(() -> {
            // Get the accounts list
            LiveData<List<AccountModel>> liveData = accountsViewModel.getAllAccounts();

            // Observe the LiveData
            mainHandler.post(() -> liveData.observe(getViewLifecycleOwner(), accountList -> {
                if (accountList == null || accountList.isEmpty()) {
                    noDataImage.setVisibility(View.VISIBLE);
                } else {
                    noDataImage.setVisibility(View.GONE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    // Set the adapter
                    AccountAdapter adapter = new AccountAdapter(getContext(), (ArrayList<AccountModel>) accountList, accountsViewModel);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    // Swipe to delete
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteAccountCallback(adapter));
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