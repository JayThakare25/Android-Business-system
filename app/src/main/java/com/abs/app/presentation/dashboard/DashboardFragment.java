package com.abs.app.presentation.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.abs.app.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import dagger.hilt.android.AndroidEntryPoint;
import androidx.navigation.Navigation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Map;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    private DashboardViewModel viewModel;
    private TextView tvRevenue, tvSales, tvRestockSuggestions;
    private View cardRestock;
    private LineChart lineChart;
    private java.util.List<com.abs.app.data.local.entity.SaleEntity> currentSales = new java.util.ArrayList<>();
    private java.util.List<com.abs.app.data.local.entity.ExpenseEntity> currentExpenses = new java.util.ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        
        tvRevenue = view.findViewById(R.id.tv_today_revenue);
        tvSales = view.findViewById(R.id.tv_total_sales);
        tvRestockSuggestions = view.findViewById(R.id.tv_restock_suggestions);
        cardRestock = view.findViewById(R.id.card_restock_alerts);
        lineChart = view.findViewById(R.id.chart_profit_trend);
        
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Timeframe Selection Logic
        com.google.android.material.button.MaterialButtonToggleGroup toggle = view.findViewById(R.id.toggle_timeframe);
        toggle.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                if (checkedId == R.id.btn_7d) viewModel.setTimeframe(7);
                else if (checkedId == R.id.btn_30d) viewModel.setTimeframe(30);
                else if (checkedId == R.id.btn_1y) viewModel.setTimeframe(365);
            }
        });

        long now = System.currentTimeMillis();
        long startOfDay = now - (now % (24 * 60 * 60 * 1000));
        
        viewModel.getTodayRevenue(startOfDay, now).observe(getViewLifecycleOwner(), revenue -> {
            tvRevenue.setText(String.format("$%.2f", revenue != null ? revenue : 0.00));
        });

        viewModel.getTotalSalesCount().observe(getViewLifecycleOwner(), count -> {
            tvSales.setText(String.valueOf(count != null ? count : 0));
        });

        viewModel.getLowStockProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null && !products.isEmpty()) {
                cardRestock.setVisibility(View.VISIBLE);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < Math.min(products.size(), 3); i++) {
                    com.abs.app.data.local.entity.ProductEntity p = products.get(i);
                    sb.append("• ").append(p.name).append(" (Only ").append(p.stockQuantity).append(" left)\n");
                }
                tvRestockSuggestions.setText(sb.toString().trim());
            } else {
                cardRestock.setVisibility(View.GONE);
            }
        });

        viewModel.getSalesForTimeframe().observe(getViewLifecycleOwner(), sales -> {
            currentSales = sales != null ? sales : new java.util.ArrayList<>();
            updateProfitChart();
        });

        viewModel.getExpensesForTimeframe().observe(getViewLifecycleOwner(), expenses -> {
            currentExpenses = expenses != null ? expenses : new java.util.ArrayList<>();
            updateProfitChart();
        });
        
        view.findViewById(R.id.btn_launch_crm).setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.action_dashboard_to_crm)
        );

        view.findViewById(R.id.btn_launch_inventory).setOnClickListener(v -> {
            // Find activity's Bottom Nav and change selected item to keep in sync
            if (getActivity() != null) {
                com.google.android.material.bottomnavigation.BottomNavigationView nav = 
                    getActivity().findViewById(R.id.bottom_navigation);
                if (nav != null) {
                    nav.setSelectedItemId(R.id.inventoryFragment);
                }
            }
        });
        
        view.findViewById(R.id.fab_ai_assistant).setOnClickListener(v -> {
            new com.abs.app.presentation.ai.AiAssistantBottomSheet().show(getChildFragmentManager(), "AiAssistant");
        });
    }

    private void updateProfitChart() {
        ArrayList<com.github.mikephil.charting.data.Entry> entries = new ArrayList<>();
        java.util.Map<String, Double> dailyDelta = new java.util.TreeMap<>();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd", java.util.Locale.getDefault());

        // Calculate Daily Net Delta
        for (com.abs.app.data.local.entity.SaleEntity s : currentSales) {
            String day = sdf.format(new java.util.Date(s.timestampMillis));
            dailyDelta.put(day, dailyDelta.getOrDefault(day, 0.0) + s.totalAmount);
        }
        for (com.abs.app.data.local.entity.ExpenseEntity e : currentExpenses) {
            String day = sdf.format(new java.util.Date(e.timestampMillis));
            dailyDelta.put(day, dailyDelta.getOrDefault(day, 0.0) - e.amount);
        }

        if (dailyDelta.isEmpty()) {
            lineChart.clear();
            return;
        }

        // Convert to Cumulative Profit to show Net Loss/Profit state
        double cumulativeBalance = 0.0;
        int index = 0;
        final String[] labels = new String[dailyDelta.size()];
        for (java.util.Map.Entry<String, Double> entry : dailyDelta.entrySet()) {
            cumulativeBalance += entry.getValue();
            entries.add(new com.github.mikephil.charting.data.Entry(index, (float) cumulativeBalance));
            labels[index] = entry.getKey();
            index++;
        }

        com.github.mikephil.charting.data.LineDataSet dataSet = new com.github.mikephil.charting.data.LineDataSet(entries, "Net Cumulative Position ($)");
        dataSet.setMode(com.github.mikephil.charting.data.LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setCubicIntensity(0.2f);
        dataSet.setDrawFilled(true);
        dataSet.setDrawCircles(true);
        dataSet.setCircleRadius(6f);
        dataSet.setCircleColor(Color.parseColor("#006497"));
        dataSet.setLineWidth(4f);
        dataSet.setColor(Color.parseColor("#006497"));
        dataSet.setFillDrawable(getResources().getDrawable(R.drawable.gradient_chart_fill));
        dataSet.setValueTextSize(12f);
        dataSet.setDrawValues(true);

        com.github.mikephil.charting.data.LineData lineData = new com.github.mikephil.charting.data.LineData(dataSet);
        lineChart.setData(lineData);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(14f);
        xAxis.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        
        lineChart.getAxisLeft().setTextSize(14f);
        lineChart.getAxisLeft().setDrawGridLines(true);
        lineChart.getAxisLeft().setGridColor(Color.parseColor("#DDDDDD"));
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        
        lineChart.getLegend().setTextSize(14f);
        lineChart.getLegend().setFormSize(14f);

        lineChart.setExtraOffsets(0, 0, 0, 10);
        lineChart.setPinchZoom(true);
        lineChart.animateX(1000);
        lineChart.invalidate();
    }
}
