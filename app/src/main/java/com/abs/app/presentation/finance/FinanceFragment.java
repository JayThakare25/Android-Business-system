package com.abs.app.presentation.finance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.abs.app.R;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FinanceFragment extends Fragment {
    private FinanceViewModel viewModel;
    private FinanceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_finance, container, false);
    }

    private double realRevenue = 0.0;
    private double realExpenses = 0.0;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(FinanceViewModel.class);
        
        android.widget.TextView tvNet = view.findViewById(R.id.tv_net_profit);
        RecyclerView rv = view.findViewById(R.id.rv_transactions);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new FinanceAdapter();
        rv.setAdapter(adapter);

        java.lang.Runnable recalculateProfit = () -> {
            double netProfit = realRevenue - realExpenses;
            tvNet.setText(String.format("$%,.2f", netProfit));
            if (netProfit < 0) {
                tvNet.setTextColor(android.graphics.Color.RED);
            } else {
                tvNet.setTextColor(android.graphics.Color.parseColor("#388E3C")); // Dark Green
            }
        };

        viewModel.getTotalRevenue().observe(getViewLifecycleOwner(), revenue -> {
            realRevenue = (revenue != null) ? revenue : 0.0;
            recalculateProfit.run();
        });

        viewModel.getAllExpenses().observe(getViewLifecycleOwner(), expenses -> {
            if (expenses != null) {
                adapter.submitList(expenses);
                realExpenses = 0.0;
                for (com.abs.app.data.local.entity.ExpenseEntity e : expenses) {
                    realExpenses += e.amount;
                }
                recalculateProfit.run();
            }
        });

        // Enhanced Manual Expense Logging
        view.findViewById(R.id.btn_add_expense).setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
            builder.setTitle("Log New Business Expense");
            
            android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(60, 20, 60, 20);
            
            final android.widget.EditText inputCategory = new android.widget.EditText(requireContext());
            inputCategory.setHint("Category (e.g. Rent, Utilities)");
            layout.addView(inputCategory);
            
            final android.widget.EditText inputAmount = new android.widget.EditText(requireContext());
            inputAmount.setHint("Amount ($)");
            inputAmount.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            layout.addView(inputAmount);

            final android.widget.EditText inputDesc = new android.widget.EditText(requireContext());
            inputDesc.setHint("Description (Brief)");
            layout.addView(inputDesc);

            builder.setView(layout);
            builder.setPositiveButton("Record Expense", (dialog, which) -> {
                String cat = inputCategory.getText().toString();
                String amtStr = inputAmount.getText().toString();
                String desc = inputDesc.getText().toString();
                
                if (!amtStr.isEmpty() && !cat.isEmpty()) {
                    com.abs.app.data.local.entity.ExpenseEntity newExp = new com.abs.app.data.local.entity.ExpenseEntity();
                    newExp.category = cat;
                    newExp.amount = Double.parseDouble(amtStr);
                    newExp.description = desc;
                    newExp.timestampMillis = System.currentTimeMillis();
                    viewModel.insertExpense(newExp);
                    android.widget.Toast.makeText(getContext(), "Logged $" + amtStr + " to " + cat, android.widget.Toast.LENGTH_SHORT).show();
                } else {
                    android.widget.Toast.makeText(getContext(), "Please fill category and amount", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });

        // Professional Invoice Preview Simulation
        view.findViewById(R.id.btn_create_invoice).setOnClickListener(v -> {
            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.layout_invoice_preview, null);
            android.app.AlertDialog previewDialog = new android.app.AlertDialog.Builder(requireContext())
                    .setView(dialogView)
                    .create();

            ((android.widget.TextView) dialogView.findViewById(R.id.tv_invoice_revenue)).setText(String.format("$%,.2f", realRevenue));
            ((android.widget.TextView) dialogView.findViewById(R.id.tv_invoice_expenses)).setText(String.format("-$%,.2f", realExpenses));
            
            double profit = realRevenue - realExpenses;
            ((android.widget.TextView) dialogView.findViewById(R.id.tv_invoice_profit)).setText(String.format("$%,.2f", profit));
            
            java.util.Date now = new java.util.Date();
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            ((android.widget.TextView) dialogView.findViewById(R.id.tv_invoice_date)).setText(df.format("MMM dd, yyyy", now));

            dialogView.findViewById(R.id.btn_close_preview).setOnClickListener(view1 -> previewDialog.dismiss());
            
            previewDialog.show();
        });
    }
}
