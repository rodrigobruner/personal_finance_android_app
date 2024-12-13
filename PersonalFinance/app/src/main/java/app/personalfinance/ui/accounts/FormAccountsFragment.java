package app.personalfinance.ui.accounts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import app.personalfinance.R;
import app.personalfinance.databinding.FragmentFormAccountsBinding;
import app.personalfinance.viewModel.accounts.AccountsViewModel;

public class FormAccountsFragment extends Fragment {

    private FragmentFormAccountsBinding binding;

    EditText editTextAccountName, editTextAccountBalance;
    Button saveButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountsViewModel accountsViewModel =
                new ViewModelProvider(this).get(AccountsViewModel.class);

        binding = FragmentFormAccountsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        editTextAccountName = binding.editTextAccountName;
        editTextAccountBalance = binding.editTextAccountBalance;
        saveButton = binding.buttonSaveAccount;

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String accountName = editTextAccountName.getText().toString();
                    String accountBalance = editTextAccountBalance.getText().toString();
                    accountsViewModel.saveAccount(accountName, Double.parseDouble(accountBalance), status -> {
                        if (status) {
                            Toast.makeText(getContext(), getString(R.string.form_account_save_msg), Toast.LENGTH_LONG).show();
                            NavController navController = Navigation.findNavController(v);
                            navController.navigate(R.id.action_formAccountsFragment_to_accountsFragment);
                        } else {
                            Toast.makeText(getContext(), getString(R.string.form_account_save_error), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), getString(R.string.form_account_save_error), Toast.LENGTH_LONG).show();
                }
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}