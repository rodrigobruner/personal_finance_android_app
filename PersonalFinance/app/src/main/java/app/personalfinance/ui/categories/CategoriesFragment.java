package app.personalfinance.ui.categories;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import app.personalfinance.R;
import app.personalfinance.data.categories.CategoryModel;
import app.personalfinance.databinding.FragmentCategoriesBinding;
import app.personalfinance.viewModel.categories.CategoriesViewModel;

public class CategoriesFragment extends Fragment {
    // Binding
    private FragmentCategoriesBinding binding;

    // ViewModel
    private CategoriesViewModel categoriesViewModel;

    // ImageView for when you have no data
    ImageView noDataImage;

    // Executor and Handler
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    // RecyclerView
    private RecyclerView recyclerView;
    // Adapter
    private CategoryAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get widget
        noDataImage = binding.imageViewCategoriesNoData;
        // Onclick of the add button redirect to the form
        Button addButton = binding.buttonAddCategory;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the form
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_categoriesFragment_to_formCategoriesFragment);
            }
        });
        // Get the RecyclerView
        recyclerView = binding.recyclerViewCategories;
        bindRecyclerView(); // Bind the RecyclerView

        return root;
    }

    private void bindRecyclerView() {
        executor.execute(() -> {
            // Get the categories list
            LiveData<List<CategoryModel>> liveData = categoriesViewModel.getAllCategories();

            // Observe the LiveData
            mainHandler.post(() -> liveData.observe(getViewLifecycleOwner(), categoryList -> {
                if (categoryList == null || categoryList.isEmpty()) { // If the list is empty
                    noDataImage.setVisibility(View.VISIBLE); // Show the no data image
                } else {
                    noDataImage.setVisibility(View.GONE); // Hide the no data image
                    // Set the layout manager
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    // Create adapter
                    adapter = new CategoryAdapter(getContext(), (ArrayList<CategoryModel>) categoryList, categoriesViewModel);
                    recyclerView.setAdapter(adapter); // Set the adapter
                    adapter.notifyDataSetChanged(); // Notify the adapter

                    // Swipe to delete
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCategoryCallback(adapter));
                    itemTouchHelper.attachToRecyclerView(recyclerView);
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