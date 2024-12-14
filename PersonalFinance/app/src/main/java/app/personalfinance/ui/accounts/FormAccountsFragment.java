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
import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.databinding.FragmentFormAccountsBinding;
import app.personalfinance.viewModel.accounts.AccountsViewModel;

public class FormAccountsFragment extends Fragment {

    // Binding
    private FragmentFormAccountsBinding binding;

    // Widgets
    private EditText editTextAccountName, editTextAccountBalance;
    private Button saveButton;
    // Account id, used in the update
    private int accountId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountsViewModel accountsViewModel =
                new ViewModelProvider(this).get(AccountsViewModel.class);

        binding = FragmentFormAccountsBinding.inflate(inflater, container, false);
        View root = binding.getRoot(); // Inflate the layout for this fragment

        // Get the widgets
        editTextAccountName = binding.editTextAccountName;
        editTextAccountBalance = binding.editTextAccountBalance;
        saveButton = binding.buttonSaveAccount;

        Bundle bundle = getArguments(); // Get the arguments
        if (bundle != null) { // If there are arguments
            // Get the account model
            AccountModel account = (AccountModel) bundle.getSerializable("accountModel");
            if (account != null) { // If the account is not null
                editTextAccountName.setText(account.getName()); // Set the account name
                // Set the account balance
                editTextAccountBalance.setText(String.valueOf(account.getBalance()));
                accountId = account.getId(); // Set the account id
            }
        }

        // Onclick of the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Get the account name and balance
                    String accountName = editTextAccountName.getText().toString();
                    String accountBalance = editTextAccountBalance.getText().toString();
                    if (accountName.isEmpty() || accountName == null || accountBalance.isEmpty() || accountBalance == null) { // If the account name or balance is empty
                        // Show an error message
                        Toast.makeText(getContext(), getString(R.string.form_account_required_field), Toast.LENGTH_LONG).show();
                        return;
                    }

                    // Save the account
                    accountsViewModel.saveAccount(accountId, accountName, Double.parseDouble(accountBalance), status -> {
                        if (status) { // If the account is saved
                            Toast.makeText(getContext(), getString(R.string.form_account_save_msg), Toast.LENGTH_LONG).show();
                            // Move to the accountsFragment
                            NavController navController = Navigation.findNavController(v);
                            navController.navigate(R.id.action_formAccountsFragment_to_accountsFragment);
                        } else {
                            // else show an error message
                            Toast.makeText(getContext(), getString(R.string.form_account_save_error), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) { // Catch the exception
                    e.printStackTrace();
                    // Show an error message
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