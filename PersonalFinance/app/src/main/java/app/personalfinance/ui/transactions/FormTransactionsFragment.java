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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import app.personalfinance.MainActivity;
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

    // Argument
    private static final String ARG_TYPE = "type";
    private String type;

    // Widgets
    private Spinner spinnerFrom, spinnerTo;
    private EditText editTextAmount, editTextDescription, editTextDate;
    private Button buttonSave;
    private TextView textViewFrom, textViewTo;

    // list of categories and accounts
    private List<CategoryModel> categories;
    private List<AccountModel> accounts;

    // Calendar
    Calendar calendar;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_transactions, container, false);
        // Get widgets
        spinnerFrom = view.findViewById(R.id.spinnerTranscationFrom);
        spinnerTo = view.findViewById(R.id.spinnerTranscationTo);
        editTextAmount = view.findViewById(R.id.editTextTranscationAmount);
        editTextDescription = view.findViewById(R.id.editTextTranscationDescription);
        editTextDate = view.findViewById(R.id.editTextTranscationDate);
        buttonSave = view.findViewById(R.id.buttonSave);
        textViewFrom = view.findViewById(R.id.textViewTranscationFrom);
        textViewTo = view.findViewById(R.id.textViewTranscationTo);

        // Hide floating action button
//        FloatingActionButton fab = ((MainActivity) getActivity()).findViewById(R.id.fab);
//        fab.setVisibility(View.GONE);
        // TODO: How to enable the floating action button again?

        numericKeyboard(view, editTextAmount);
        //Set current date
        clearForm();

        //Create date picker
        calendar = Calendar.getInstance();
        //Set date picker
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);
                //Format date
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                editTextDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        //Set date picker
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Load categories and accounts
        setupSpinners();

        //TODO: Refactor this
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Get current tab position
                    ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                    int currentTabPosition = viewPager.getCurrentItem();

                    // Get data from form
                    Double amount = CurrencyFormatter.convert(editTextAmount.getText().toString());
                    String description = editTextDescription.getText().toString();
                    String date = editTextDate.getText().toString();

                    // Check if required fields are filled
                    if (amount <= 0 || date.isEmpty()) {
                        Toast.makeText(getContext(), getString(R.string.form_transaction_required_field), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Get category and account
                    int category = 0;
                    int account = 0;

                    // If is income, category and account are loaded
                    if (currentTabPosition == 0) {
                        type = CategoriesTypes.INCOME;
                        category = categories.get(spinnerFrom.getSelectedItemPosition()).getId();
                        account = accounts.get(spinnerTo.getSelectedItemPosition()).getId();
                    }

                    // If is expense, category and account are loaded
                    if (currentTabPosition == 1) {
                        category = categories.get(spinnerTo.getSelectedItemPosition()).getId();
                        account = accounts.get(spinnerFrom.getSelectedItemPosition()).getId();
                        type = CategoriesTypes.EXPENSE;
                    }

                    // If is a transaction (income or expense)
                    if (currentTabPosition == 0 || currentTabPosition == 1) {
                        // Get transaction view model
                        TransactionsViewModel transactionsViewModel = new ViewModelProvider(FormTransactionsFragment.this).get(TransactionsViewModel.class);
                        // Insert transaction
                        int finalAccount = account;
                        transactionsViewModel.insertTransaction(account, category, amount, description, date, type, status -> {
                            if (status) {
                                // Is to subtract or add amount
                                Double amountUpdate = currentTabPosition == 0 ? amount : -amount;
                                // Get account view model
                                AccountsViewModel accountsViewModel = new ViewModelProvider(FormTransactionsFragment.this).get(AccountsViewModel.class);
                                // Update account balance
                                accountsViewModel.updateAccountBalance(finalAccount, amountUpdate, balanceStatus -> {
                                    if (balanceStatus) {
                                        // Check if is income or expense and get success message
                                        String msg = currentTabPosition == 0 ? getString(R.string.income_insert) : getString(R.string.expense_insert);
                                        // Show success message
                                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                        clearForm(); // Clear form

                                        // Navigate to home
                                        NavController navController = Navigation.findNavController(view);
                                        navController.navigate(R.id.action_formTransactionsFragment_to_homeFragment);
                                    } else {
                                        Toast.makeText(getContext(), getString(R.string.transactions_insert_error), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), getString(R.string.transactions_insert_error), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    // If is a transfer
                    if (currentTabPosition == 2) {
                        // Get account from and to
                        int fromAccount = accounts.get(spinnerFrom.getSelectedItemPosition()).getId();
                        int toAccount = accounts.get(spinnerTo.getSelectedItemPosition()).getId();

                        // Check if account from and to are the same
                        if (fromAccount == toAccount) {
                            Toast.makeText(getContext(), getString(R.string.form_transfer_from_same_account), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get transfer view model
                        TransfersViewModel transfersViewModel = new ViewModelProvider(FormTransactionsFragment.this).get(TransfersViewModel.class);
                        // Insert transfer
                        transfersViewModel.insertTransfer(fromAccount, toAccount, amount, description, date, status -> {
                            if (status) {
                                // Get account view model
                                AccountsViewModel accountsViewModel = new ViewModelProvider(FormTransactionsFragment.this).get(AccountsViewModel.class);
                                // Update account balances
                                accountsViewModel.updateAccountBalance(fromAccount, amount * -1, fromStatus -> {
                                    if (fromStatus) {
                                        accountsViewModel.updateAccountBalance(toAccount, amount, toStatus -> {
                                            if (toStatus) {
                                                // Show success message
                                                Toast.makeText(getContext(), getString(R.string.transfer_insert), Toast.LENGTH_SHORT).show();

                                                // Navigate to home
                                                NavController navController = Navigation.findNavController(view);
                                                navController.navigate(R.id.action_formTransactionsFragment_to_homeFragment);
                                            } else {
                                                Toast.makeText(getContext(), getString(R.string.transactions_insert_error), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getContext(), getString(R.string.transactions_insert_error), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), getString(R.string.transactions_insert_error), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    // Show erro message
                    Toast.makeText(getContext(), getString(R.string.transactions_insert_error), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    //To pass the type as argument
    public static FormTransactionsFragment newInstance(String type) {
        // Create a new instance of FormTransactionsFragment
        FormTransactionsFragment fragment = new FormTransactionsFragment();
        // Set the argument
        Bundle args = new Bundle();
        // Set the type to pass as argument
        args.putString(ARG_TYPE, type);
        // Set the arguments
        fragment.setArguments(args);
        return fragment;
    }

    // Numeric keyboard for amount
    private void numericKeyboard(View view, EditText target) {
        //keyboard buttons list
        int[] numberButtonIds = {
                R.id.buttonNumericKeypad0,
                R.id.buttonNumericKeypad1,
                R.id.buttonNumericKeypad2,
                R.id.buttonNumericKeypad3,
                R.id.buttonNumericKeypad4,
                R.id.buttonNumericKeypad5,
                R.id.buttonNumericKeypad6,
                R.id.buttonNumericKeypad7,
                R.id.buttonNumericKeypad8,
                R.id.buttonNumericKeypad9,
                R.id.buttonNumericKeypadDot
        };
        //for each button, set the listener
        for (int id : numberButtonIds) {
            //get the button
            view.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button button = (Button) v; //get the button
                    String currentText = target.getText().toString(); //get the current text
                    target.setText(currentText + button.getText().toString()); //append the button text
                }
            });
        }

        //delete button
        Button buttonDelete = view.findViewById(R.id.buttonNumericKeypadDelete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = target.getText().toString(); //get the current text
                if (!currentText.isEmpty()) { //if the text is not empty
                    target.setText(currentText.substring(0, currentText.length() - 1)); //remove the last character
                }
            }
        });

    }

    //Clear form
    private void clearForm() {
        editTextAmount.setText("");
        editTextDescription.setText("");
        //Set current date
        editTextDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
    }

    // Setup spinners
    void setupSpinners() {
        // Get current tab position
        ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
        int currentTabPosition = viewPager.getCurrentItem();

        //if income
        if (currentTabPosition == 0) {
            //set the labels
            textViewFrom.setText(getString(R.string.form_transaction_category));
            textViewTo.setText(getString(R.string.form_transaction_account));
            //load categories and accounts
            loadCategories(CategoriesTypes.INCOME, spinnerFrom);
            loadAccounts(spinnerTo);
        }
        //if expense
        if (currentTabPosition == 1) {
            //set the labels
            textViewFrom.setText(getString(R.string.form_transaction_account));
            textViewTo.setText(getString(R.string.form_transaction_category));
            //load categories and accounts
            loadCategories(CategoriesTypes.EXPENSE, spinnerTo);
            loadAccounts(spinnerFrom);
        }
        //if transfer
        if (currentTabPosition == 2) {
            //set the labels
            textViewFrom.setText(getString(R.string.form_transaction_from));
            textViewTo.setText(getString(R.string.form_transaction_to));
            //load accounts
            loadAccounts(spinnerFrom);
            loadAccounts(spinnerTo);
        }
    }

    // Load categories
    void loadCategories(String type, Spinner spinner) {
        // Get categories view model
        CategoriesViewModel categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        // Get categories by type
        categoriesViewModel.getCategoriesByType(type).observe(getViewLifecycleOwner(), categories -> {
            if (categories != null) { //if categories are not null
                this.categories = categories; //set the categories
                List<String> categoryNames = new ArrayList<>(); //create a list of category names
                for (CategoryModel category : categories) { //for each category
                    categoryNames.add(category.getName()); //add the category name to the list
                }
                //create an adapter for the spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
                //setup the adapter
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //set the adapter
                spinner.setAdapter(adapter);
            }
        });
    }

    // Load accounts, same as loadCategories
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