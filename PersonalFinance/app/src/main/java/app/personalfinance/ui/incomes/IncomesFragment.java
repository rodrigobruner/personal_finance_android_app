package app.personalfinance.ui.incomes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import app.personalfinance.databinding.FragmentIncomesBinding;
import app.personalfinance.viewModel.incomes.IncomesViewModel;


public class IncomesFragment extends Fragment {
    private FragmentIncomesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        IncomesViewModel incomesViewModel =
                new ViewModelProvider(this).get(IncomesViewModel.class);

        binding = FragmentIncomesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textIncomes;
        incomesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
