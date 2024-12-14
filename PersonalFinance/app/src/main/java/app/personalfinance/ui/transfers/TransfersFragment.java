package app.personalfinance.ui.transfers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import app.personalfinance.data.transfers.TransferWithDetails;
import app.personalfinance.databinding.FragmentTransfersBinding;
import app.personalfinance.viewModel.transfers.TransfersViewModel;

// Fragment for the transfers list
public class TransfersFragment extends Fragment {
    // Binding
    private FragmentTransfersBinding binding;
    // ViewModel
    private TransfersViewModel transfersViewModel;
    // ImageView for when there is no data
    ImageView noDataImage;

    // RecyclerView
    private RecyclerView recyclerView;

    // Executor service to run the queries in background
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        transfersViewModel = new ViewModelProvider(this).get(TransfersViewModel.class);

        // Inflate the layout for this fragment
        binding = FragmentTransfersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get the button to add a transfer
        Button addButton = binding.buttonAddTransfers;
        noDataImage = binding.imageViewTransfersNoData;

        // Onclick
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_transfersFragment_to_transactionsFragment);
                // Move to the formTransfersFragment
            }
        });

        // RecyclerView
        recyclerView = binding.recyclerViewTransfers;
        // Set the layout manager

        // Bind the RecyclerView
        bindRecyclerView();

        return root;
    }

    void bindRecyclerView() {
        executor.execute(() -> {
            // Get the transfers list
            LiveData<List<TransferWithDetails>> liveData = transfersViewModel.getAllTransfers();

            // Observe the LiveData
            mainHandler.post(() -> liveData.observe(getViewLifecycleOwner(), transferList -> {
                if (transferList == null || transferList.isEmpty()) {
                    noDataImage.setVisibility(View.VISIBLE);
                    Log.d("TransfersFragment", "Transfer list is null");
                } else {
                    noDataImage.setVisibility(View.GONE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    // Set the adapter
                    TransferAdapter adapter = new TransferAdapter(getContext(), (ArrayList<TransferWithDetails>) transferList, transfersViewModel);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    Log.d("TransfersFragment", "Adapter updated with transfers size: " + adapter.getItemCount());

                    // Swipe to delete
                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteTransferCallback(adapter));
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