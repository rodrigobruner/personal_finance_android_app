package app.personalfinance.ui.transactions;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FormPagerAdapter extends FragmentStateAdapter {
    public static final String[] TAB_TITLES = new String[]{"Income", "Expense", "Transfer"};

    public FormPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return FormTransactionsFragment.newInstance(TAB_TITLES[position]);
    }

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }
}