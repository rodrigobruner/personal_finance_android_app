package app.personalfinance.ui.transactions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import app.personalfinance.databinding.FragmentTransactionsBinding;
import app.personalfinance.viewModel.transactions.TransactionsViewModel;

public class TransactionsFragment extends Fragment {
    private FragmentTransactionsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TransactionsViewModel transactionsViewModel =
                new ViewModelProvider(this).get(TransactionsViewModel.class);

        binding = FragmentTransactionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get the widgets
        TabLayout tabLayout = binding.tabLayout;
        ViewPager2 viewPager = binding.viewPager;
        // set up the view pager
        FormPagerAdapter adapter = new FormPagerAdapter(requireActivity());
        // set the adapter
        viewPager.setAdapter(adapter);
        // set up the tab layout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(FormPagerAdapter.TAB_TITLES[position])).attach();
        viewPager.setCurrentItem(1, false);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
