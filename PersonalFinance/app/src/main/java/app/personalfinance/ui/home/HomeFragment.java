package app.personalfinance.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.databinding.FragmentHomeBinding;
import app.personalfinance.viewModel.accounts.AccountsViewModel;
import app.personalfinance.viewModel.home.HomeViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RecyclerView recyclerViewAccounts;

    private AccountsViewModel accountsViewModel;

    // Executor service to run the queries in background
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private ImageView imageViewHomeNoAccount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerViewAccounts = binding.recyclerViewAccounts;

        imageViewHomeNoAccount = binding.imageViewHomeNoAccount;

        accountsViewModel = new ViewModelProvider(this).get(AccountsViewModel.class);
        loadAccounts();


        return root;
    }


    private void loadAccounts() {
        executor.execute(() -> {
            // Get the accounts list
            LiveData<List<AccountModel>> liveData = accountsViewModel.getAllAccounts();

            // Observe the LiveData
            mainHandler.post(() -> liveData.observe(getViewLifecycleOwner(), accounts -> {

                imageViewHomeNoAccount.setVisibility(View.VISIBLE);
                recyclerViewAccounts.setVisibility(View.GONE);
                if (accounts != null && !accounts.isEmpty()) {
                    imageViewHomeNoAccount.setVisibility(View.GONE);
                    recyclerViewAccounts.setVisibility(View.VISIBLE);

                    Log.d("HomeFragment", "Accounts: " + accounts.size());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewAccounts.setLayoutManager(layoutManager);
                    AccountsCardAdapter adapter = new AccountsCardAdapter(getContext(), accounts);
                    recyclerViewAccounts.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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