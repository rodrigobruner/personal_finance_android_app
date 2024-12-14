package app.personalfinance.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import app.personalfinance.R;
import app.personalfinance.data.categories.CategoryModel;
import app.personalfinance.viewModel.categories.CategoriesViewModel;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    // Category list
    private List<CategoryModel> categories;
    // Context
    private Context context;
    // Categories view model
    private CategoriesViewModel categoriesViewModel;

    // Constructor
    public CategoryAdapter(Context context,
                           List<CategoryModel> categories,
                           CategoriesViewModel categoriesViewModel) {
        this.context = context;
        this.categories = categories;
        this.categoriesViewModel = categoriesViewModel;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.listview_categories, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = categories.get(position); // Get category
        // Set category name and type
        holder.categoryName.setText(category.getName());
        holder.categoryType.setText(category.getType());
        // Set category type color, default is green
        holder.categoryType.setTextColor(context.getResources().getColor(R.color.green));
        // If category type is expense, set color to red
        if (category.getType().equalsIgnoreCase("expense")) {
            holder.categoryType.setTextColor(context.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    // Update categories
    public void updateCategories(List<CategoryModel> newCategories) {
        this.categories.clear();
        this.categories.addAll(newCategories);
        notifyDataSetChanged();
    }

    // Delete category
    public void deleteItem(int position) {
        try {
            // Get category
            CategoryModel category = categories.get(position);
            // Delete category
            categoriesViewModel.deleteCategory(category, status -> {
                if (status) { // If success
                    categories.remove(position); // Remove category
                    notifyItemRemoved(position); // Notify item removed
                    // Show message
                    Toast.makeText(context, R.string.category_delete, Toast.LENGTH_SHORT).show();
                } else {
                    // if failed, show error message
                    Toast.makeText(context, R.string.category_delete_error, Toast.LENGTH_SHORT).show();
                }
                this.notifyDataSetChanged(); // Notify data set changed
            });
        } catch (Exception e) { // If error, show error message
            Toast.makeText(context, R.string.category_delete_error, Toast.LENGTH_SHORT).show();
        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        //define widgets
        TextView categoryName;
        TextView categoryType;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            //set widgets
            categoryName = itemView.findViewById(R.id.textViewCategoryName);
            categoryType = itemView.findViewById(R.id.textViewCategoryType);
        }
    }
}