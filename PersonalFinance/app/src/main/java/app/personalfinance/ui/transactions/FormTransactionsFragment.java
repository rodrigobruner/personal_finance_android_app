package app.personalfinance.ui.transactions;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import app.personalfinance.R;
import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.data.categories.CategoriesTypes;
import app.personalfinance.data.categories.CategoryModel;
import app.personalfinance.viewModel.accounts.AccountsViewModel;
import app.personalfinance.viewModel.categories.CategoriesViewModel;

public class FormTransactionsFragment extends Fragment {

    private static final String ARG_TYPE = "type";
    private String type;

    private Spinner spinnerFrom, spinnerTo;
    private EditText editTextAmount, editTextDescription, editTextDate;
    private Button buttonSave;
    private TextView textViewFrom, textViewTo;

    public static FormTransactionsFragment newInstance(String type) {
        FormTransactionsFragment fragment = new FormTransactionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_transactions, container, false);

        spinnerFrom = view.findViewById(R.id.spinnerTranscationFrom);
        spinnerTo = view.findViewById(R.id.spinnerTranscationTo);
        editTextAmount = view.findViewById(R.id.editTextTranscationAmount);
        editTextDescription = view.findViewById(R.id.editTextTranscationDescription);
        editTextDate = view.findViewById(R.id.editTextTranscationDate);
        buttonSave = view.findViewById(R.id.buttonSave);
        textViewFrom = view.findViewById(R.id.textViewTranscationFrom);
        textViewTo = view.findViewById(R.id.textViewTranscationTo);

        setupSpinners();

        return view;
    }

    void setupSpinners() {
        ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
        int currentTabPosition = viewPager.getCurrentItem();

        Log.d("FormTransactionsFragment", "Current Tab Position: " + currentTabPosition);

        if (currentTabPosition == 0) {
            textViewFrom.setText(getString(R.string.form_transaction_category));
            textViewTo.setText(getString(R.string.form_transaction_account));
            loadCategories(CategoriesTypes.INCOME, spinnerFrom);
            loadAccounts(spinnerTo);
        }
        if (currentTabPosition == 1) {
            textViewFrom.setText(getString(R.string.form_transaction_account));
            textViewTo.setText(getString(R.string.form_transaction_category));
            loadCategories(CategoriesTypes.EXPENSE, spinnerTo);
            loadAccounts(spinnerFrom);
        }

        if (currentTabPosition == 2) {
            textViewFrom.setText(getString(R.string.form_transaction_from));
            textViewTo.setText(getString(R.string.form_transaction_to));
            loadAccounts(spinnerFrom);
            loadAccounts(spinnerTo);
        }
    }

    void loadCategories(String type, Spinner spinner) {
        CategoriesViewModel categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        categoriesViewModel.getCategoriesByType(type).observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) {
                List<String> categoryNames = new ArrayList<>();
                for (CategoryModel category : categories) {
                    Log.i("Category", "Category: " + category.getName());
                    categoryNames.add(category.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        });
    }

    void loadAccounts(Spinner spinner) {
        AccountsViewModel accountsViewModel = new ViewModelProvider(this).get(AccountsViewModel.class);
        accountsViewModel.getAllAccounts().observe(getViewLifecycleOwner(), accounts -> {
            if (accounts != null) {
                List<String> accountNames = new ArrayList<>();
                for (AccountModel account : accounts) {
                    Log.i("Account", "Account: " + account.getName());
                    accountNames.add(account.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, accountNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        });
    }
}