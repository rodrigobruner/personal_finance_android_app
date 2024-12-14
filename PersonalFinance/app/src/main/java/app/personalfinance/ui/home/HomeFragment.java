package app.personalfinance.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import app.personalfinance.MainActivity;
import app.personalfinance.R;
import app.personalfinance.data.accounts.AccountModel;
import app.personalfinance.data.categories.CategoriesTypes;
import app.personalfinance.data.helpper.DataChartLabelValueModel;
import app.personalfinance.databinding.FragmentHomeBinding;
import app.personalfinance.viewModel.accounts.AccountsViewModel;
import app.personalfinance.viewModel.transactions.TransactionsViewModel;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private RecyclerView recyclerViewAccounts;

    private AccountsViewModel accountsViewModel;

    // Executor service to run the queries in background
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private ImageView imageViewHomeNoAccount;

    private TransactionsViewModel transactionsViewModel;

    private PieChart expensesChart;
    private PieChart incomesChart;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerViewAccounts = binding.recyclerViewAccounts;

        imageViewHomeNoAccount = binding.imageViewHomeNoAccount;

        accountsViewModel = new ViewModelProvider(this).get(AccountsViewModel.class);
        loadAccounts();

        expensesChart = root.findViewById(R.id.expensesChart);
        incomesChart = root.findViewById(R.id.incomesChart);

        setupPieChart(expensesChart, CategoriesTypes.EXPENSE);
        loadPieChartData(expensesChart, CategoriesTypes.EXPENSE);

        setupPieChart(incomesChart, CategoriesTypes.INCOME);
        loadPieChartData(incomesChart, CategoriesTypes.INCOME);

        transactionsViewModel = new ViewModelProvider(this).get(TransactionsViewModel.class);

        return root;
    }


    private void loadAccounts() {
        executor.execute(() -> {
            // Get the accounts list
            LiveData<List<AccountModel>> liveData = accountsViewModel.getAllAccounts();

            // Observe the LiveData
            mainHandler.post(() -> liveData.observe(getViewLifecycleOwner(), accounts -> {

                imageViewHomeNoAccount.setVisibility(View.VISIBLE);
                recyclerViewAccounts.setVisibility(View.GONE);
                if (accounts != null && !accounts.isEmpty()) {
                    imageViewHomeNoAccount.setVisibility(View.GONE);
                    recyclerViewAccounts.setVisibility(View.VISIBLE);

                    Log.d("HomeFragment", "Accounts: " + accounts.size());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewAccounts.setLayoutManager(layoutManager);
                    AccountsCardAdapter adapter = new AccountsCardAdapter(getContext(), accounts);
                    recyclerViewAccounts.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }));
        });
    }



    private void setupPieChart(PieChart chart, String centerText) {
        chart.setUsePercentValues(false);
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(0, 10, 0, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setCenterText(centerText);
        chart.setCenterTextSize(20);
        chart.setCenterTextOffset(0, -20);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(getResources().getColor(R.color.white));
        chart.setTransparentCircleColor(getResources().getColor(R.color.white));
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        chart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        chart.setEntryLabelColor(getResources().getColor(R.color.black));
        chart.setEntryLabelTextSize(12f);

        chart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e == null)
                    return;
                Log.i("VAL SELECTED",
                        "Value: " + e.getY() + ", index: " + h.getX()
                                + ", DataSet index: " + h.getDataSetIndex());
            }

            @Override
            public void onNothingSelected() {
                Log.i("PieChart", "nothing selected");
            }
        });
    }

    private void loadPieChartData(PieChart chart, String type) {
        executor.execute(() -> {
            LiveData<List<DataChartLabelValueModel>> liveData = transactionsViewModel.getCurrentMonthSummaryByType(type);

            mainHandler.post(() -> liveData.observe(getViewLifecycleOwner(), dataList -> {
                if (dataList != null && !dataList.isEmpty()) {
                    ArrayList<PieEntry> entries = new ArrayList<>();
                    for (DataChartLabelValueModel data : dataList) {
                        entries.add(new PieEntry(data.getValue(), data.getLabel()));
                    }

                    PieDataSet dataSet = new PieDataSet(entries, type);
                    dataSet.setSliceSpace(3f);
                    dataSet.setSelectionShift(5f);
                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

                    PieData data = new PieData(dataSet);
                    data.setValueTextSize(10f);
                    data.setValueTextColor(Color.YELLOW);

                    chart.setData(data);
                    chart.invalidate(); // refresh the chart
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