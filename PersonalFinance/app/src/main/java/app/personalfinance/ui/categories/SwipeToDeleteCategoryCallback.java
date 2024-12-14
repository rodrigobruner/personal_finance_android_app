package app.personalfinance.ui.categories;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

// Callback for swipe to delete category
public class SwipeToDeleteCategoryCallback extends ItemTouchHelper.SimpleCallback {

    private CategoryAdapter mAdapter;

    public SwipeToDeleteCategoryCallback(CategoryAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        mAdapter.deleteItem(position);
    }
}