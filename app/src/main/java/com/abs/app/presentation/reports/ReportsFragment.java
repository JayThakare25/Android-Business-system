package com.abs.app.presentation.reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.abs.app.R;
import java.io.File;

public class ReportsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);
        
        Button btnSalesPdf = view.findViewById(R.id.btn_generate_sales_pdf);
        btnSalesPdf.setOnClickListener(v -> {
            File pdf = ReportGenerator.generatePdfReport(requireContext(), "Weekly Sales Report", "Total Revenue: $2,500.00\nTop Item: Wireless Mouse\nPending Invoices: 2");
            Toast.makeText(getContext(), "Report Saved to: " + pdf.getAbsolutePath(), Toast.LENGTH_LONG).show();
        });
        
        return view;
    }
}
