package app.personalfinance.ui.transfers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import app.personalfinance.databinding.FragmentTransfersBinding;
import app.personalfinance.ui.transfers.TransfersViewModel;


public class TransfersFragment extends Fragment {

    private FragmentTransfersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TransfersViewModel transfersViewModel =
                new ViewModelProvider(this).get(TransfersViewModel.class);

        binding = FragmentTransfersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTransfers;
        transfersViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
