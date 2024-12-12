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

    ImageView noDataImage;

    // Executor and Handler
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private RecyclerView recyclerView;
    private CategoryAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        categoriesViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);

        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        noDataImage = binding.imageViewCategoriesNoData;

        Button addButton = binding.buttonAddCategory;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_categoriesFragment_to_formCategoriesFragment);
            }
        });

        recyclerView = binding.recyclerViewCategories;
        bindRecyclerView();

        return root;
    }

    private void bindRecyclerView() {
        executor.execute(() -> {
            // Get the categories list
            LiveData<List<CategoryModel>> liveData = categoriesViewModel.getAllCategories();

            // Observe the LiveData
            mainHandler.post(() -> liveData.observe(getViewLifecycleOwner(), categoryList -> {
                if (categoryList == null || categoryList.isEmpty()) {
                    noDataImage.setVisibility(View.VISIBLE);
                } else {
                    noDataImage.setVisibility(View.GONE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new CategoryAdapter(getContext(), (ArrayList<CategoryModel>) categoryList, categoriesViewModel);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

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