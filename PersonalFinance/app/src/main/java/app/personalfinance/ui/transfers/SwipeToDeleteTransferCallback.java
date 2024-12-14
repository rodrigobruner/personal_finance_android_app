package app.personalfinance.ui.transfers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

// SwipeToDeleteTransferCallback is a SimpleCallback that allows the user to swipe left or right to delete a transfer
public class SwipeToDeleteTransferCallback extends ItemTouchHelper.SimpleCallback {

    private TransferAdapter mAdapter;

    public SwipeToDeleteTransferCallback(TransferAdapter adapter) {
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