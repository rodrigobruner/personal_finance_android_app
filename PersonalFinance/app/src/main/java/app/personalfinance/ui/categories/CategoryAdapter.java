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
    private List<CategoryModel> categories;
    private Context context;
    private CategoriesViewModel categoriesViewModel;

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
        CategoryModel category = categories.get(position);
        holder.categoryName.setText(category.getName());
        holder.categoryType.setText(category.getType());

        holder.categoryType.setTextColor(context.getResources().getColor(R.color.green));
        if (category.getType().equalsIgnoreCase("expense")) {
            holder.categoryType.setTextColor(context.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public void updateCategories(List<CategoryModel> newCategories) {
        this.categories.clear();
        this.categories.addAll(newCategories);
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        try {
            CategoryModel category = categories.get(position);
            categoriesViewModel.deleteCategory(category);
            categories.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, R.string.category_delete, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, R.string.category_delete_error, Toast.LENGTH_SHORT).show();
        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName;
        TextView categoryType;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryName = itemView.findViewById(R.id.textViewCategoryName);
            categoryType = itemView.findViewById(R.id.textViewCategoryType);
        }
    }
}