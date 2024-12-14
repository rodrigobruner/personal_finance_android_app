package app.personalfinance.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import app.personalfinance.R;
import app.personalfinance.data.categories.CategoriesTypes;
import app.personalfinance.databinding.FragmentFormCategoriesBinding;
import app.personalfinance.viewModel.categories.CategoriesViewModel;

public class FormCategoriesFragment extends Fragment {
    // Binding
    private FragmentFormCategoriesBinding binding;
    // Widgets
    EditText editTextCategoryName;
    Button saveButton;
    RadioGroup radioGroupCategoryType;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CategoriesViewModel categoriesViewModel =
                new ViewModelProvider(this).get(CategoriesViewModel.class);

        binding = FragmentFormCategoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Get widgets
        editTextCategoryName = binding.editTextCategoryName;
        saveButton = binding.buttonSaveCategory;
        radioGroupCategoryType = binding.radioGroupCategoryType;
        // Set on click listener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Get category name and type
                    String categoryName = editTextCategoryName.getText().toString();
                    String categoryType = radioGroupCategoryType.getCheckedRadioButtonId() == R.id.radioButtonCategoryIncome
                            ? CategoriesTypes.INCOME : CategoriesTypes.EXPENSE;
                    // Save category
                    categoriesViewModel.saveCategory(categoryName, categoryType, status -> {
                        if (status) { // If category is saved
                            // Show message
                            Toast.makeText(getContext(), getString(R.string.form_category_save_msg), Toast.LENGTH_LONG).show();
                            // Redirect to categories fragment
                            NavController navController = Navigation.findNavController(v);
                            navController.navigate(R.id.action_formCategoriesFragment_to_categoriesFragment);
                        } else { // If category is not saved
                            // Show error message
                            Toast.makeText(getContext(), getString(R.string.form_category_save_error), Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (Exception e) { // If error, show error message
                    e.printStackTrace();
                    Toast.makeText(getContext(), getString(R.string.form_category_save_error), Toast.LENGTH_LONG).show();
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