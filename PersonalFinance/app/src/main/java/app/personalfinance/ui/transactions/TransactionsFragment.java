package app.personalfinance.ui.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import app.personalfinance.databinding.FragmentTransactionsBinding;
import app.personalfinance.viewModel.transactions.TransactionsViewModel;


public class TransactionsFragment extends Fragment {
    private FragmentTransactionsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TransactionsViewModel transactionsViewModel =
                new ViewModelProvider(this).get(TransactionsViewModel.class);

        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTransactions;
        transactionsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
