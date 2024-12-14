package app.personalfinance.ui.accounts;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

// Class to handdle the swipe to delete in the accounts list
public class SwipeToDeleteAccountCallback extends ItemTouchHelper.SimpleCallback {

    private AccountAdapter mAdapter;

    public SwipeToDeleteAccountCallback(AccountAdapter adapter) {
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