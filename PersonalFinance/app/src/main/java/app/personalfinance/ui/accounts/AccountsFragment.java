package app.personalfinance.ui.accounts;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import app.personalfinance.ui.accounts.AccountAdapter;
import app.personalfinance.ui.accounts.SwipeToDeleteCallback;
import app.personalfinance.viewModel.accounts.AccountsViewModel;

public class AccountsFragment extends Fragment {

    private FragmentAccountsBinding binding;
    private AccountsViewModel accountsViewModel;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountsViewModel = new ViewModelProvider(this).get(AccountsViewModel.class);

        binding = FragmentAccountsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button addButton = binding.buttonAddAccount;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_accountsFragment_to_formAccountsFragment);
            }
        });

        RecyclerView recyclerView = binding.recyclerViewAccounts;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        AccountAdapter adapter = new AccountAdapter(getContext(), new ArrayList<>(), accountsViewModel);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        bindRecyclerView(adapter);

        return root;
    }

    void bindRecyclerView(AccountAdapter adapter) {
        executor.submit(() -> {
            LiveData<List<AccountModel>> liveData = accountsViewModel.getAllAccounts();
            mainHandler.post(() -> {
                liveData.observe(getViewLifecycleOwner(), accountList -> {
                    Log.d("AccountsFragment", "Account list: " + accountList);
                    if (accountList != null) {
                        adapter.accounts.clear();
                        adapter.accounts.addAll(accountList);
                        adapter.notifyDataSetChanged();
                    }
                });
            });
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}