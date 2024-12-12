package app.personalfinance.ui.transactions;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import app.personalfinance.R;
import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.data.categories.CategoriesTypes;
import app.personalfinance.data.categories.CategoryModel;
import app.personalfinance.helpper.CurrencyFormatter;
import app.personalfinance.viewModel.accounts.AccountsViewModel;
import app.personalfinance.viewModel.categories.CategoriesViewModel;
import app.personalfinance.viewModel.transactions.TransactionsViewModel;
import app.personalfinance.viewModel.transfers.TransfersViewModel;

public class FormTransactionsFragment extends Fragment {

    private static final String ARG_TYPE = "type";
    private String type;

    private Spinner spinnerFrom, spinnerTo;
    private EditText editTextAmount, editTextDescription, editTextDate;
    private Button buttonSave;
    private TextView textViewFrom, textViewTo;

    private List<CategoryModel> categories;
    private List<AccountModel> accounts;
    Calendar calendar;

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

        //Create date picker
        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                editTextDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        setupSpinners();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                    int currentTabPosition = viewPager.getCurrentItem();

                    int category = 0;
                    int account = 0;

                    if (currentTabPosition == 0) {
                        type = CategoriesTypes.INCOME;
                        category = categories.get(spinnerFrom.getSelectedItemPosition()).getId();
                        account = accounts.get(spinnerTo.getSelectedItemPosition()).getId();
                    }

                    if (currentTabPosition == 1) {
                        category = categories.get(spinnerTo.getSelectedItemPosition()).getId();
                        account = accounts.get(spinnerFrom.getSelectedItemPosition()).getId();
                        type = CategoriesTypes.EXPENSE;
                    }

                    Double amount = CurrencyFormatter.convert(editTextAmount.getText().toString());
                    String description = editTextDescription.getText().toString();
                    String date = editTextDate.getText().toString();

                    if (amount <= 0 || date.isEmpty()) {
                        Toast.makeText(getContext(), getString(R.string.form_transaction_required_field), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (currentTabPosition == 0 || currentTabPosition == 1) {
                        TransactionsViewModel transactionsViewModel = new ViewModelProvider(FormTransactionsFragment.this).get(TransactionsViewModel.class);
                        transactionsViewModel.insertTransaction(account, category, amount, description, date, type);

                        Double amountUpdate = currentTabPosition == 0 ? amount : -amount;

                        AccountsViewModel accountsViewModel = new ViewModelProvider(FormTransactionsFragment.this).get(AccountsViewModel.class);
                        accountsViewModel.updateAccountBalance(account, amountUpdate);


                        String msg = currentTabPosition == 0 ? getString(R.string.income_insert) :
                                                                getString(R.string.expense_insert);
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    if (currentTabPosition == 2) {
                        int accountFrom = accounts.get(spinnerFrom.getSelectedItemPosition()).getId();
                        int toAccount = accounts.get(spinnerTo.getSelectedItemPosition()).getId();

                        if (accountFrom == toAccount) {
                            Toast.makeText(getContext(), getString(R.string.form_transfer_from_same_account), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        TransfersViewModel transfersViewModel = new ViewModelProvider(FormTransactionsFragment.this).get(TransfersViewModel.class);
                        transfersViewModel.insertTransfer(accountFrom, toAccount, amount, description, date);

                        Toast.makeText(getContext(), getString(R.string.transfer_insert), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getContext(), getString(R.string.transactions_insert_error), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        return view;
    }



    void setupSpinners() {
        ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
        int currentTabPosition = viewPager.getCurrentItem();

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
            Toast.makeText(getContext(), "2", Toast.LENGTH_SHORT).show();
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
                this.categories = categories;
                List<String> categoryNames = new ArrayList<>();
                for (CategoryModel category : categories) {
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
                this.accounts = accounts;
                List<String> accountNames = new ArrayList<>();
                for (AccountModel account : accounts) {
                    accountNames.add(account.getName());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, accountNames);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }
        });
    }
}