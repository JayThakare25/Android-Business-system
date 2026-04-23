package com.abs.app.presentation.hr;

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
public class HRFragment extends Fragment {
    private HrViewModel viewModel;
    private HrAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HrViewModel.class);
        
        RecyclerView rv = view.findViewById(R.id.rv_employees);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new HrAdapter();
        rv.setAdapter(adapter);

        adapter.setOnEmployeeDeleteListener(employee -> {
            viewModel.deleteEmployee(employee);
            android.widget.Toast.makeText(getContext(), employee.name + " deleted", android.widget.Toast.LENGTH_SHORT).show();
        });

        viewModel.getAllEmployees().observe(getViewLifecycleOwner(), employees -> {
            if (employees != null) adapter.submitList(employees);
        });

        viewModel.getClockInStatus().observe(getViewLifecycleOwner(), status -> {
            if (status != null) {
                android.widget.Toast.makeText(getContext(), status, android.widget.Toast.LENGTH_LONG).show();
            }
        });

        // Unified Kiosk Master Entry with Distinct Buttons
        view.findViewById(R.id.btn_kiosk_mode).setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
            builder.setTitle("HR Management Kiosk");
            
            android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(50, 40, 50, 40);
            
            android.widget.Button btnRegister = new android.widget.Button(requireContext());
            btnRegister.setText("A: Onboard New Hire");
            btnRegister.setAllCaps(false);
            btnRegister.setOnClickListener(v1 -> {
                showOnboardingDialog();
            });
            layout.addView(btnRegister);
            
            android.view.View spacer = new android.view.View(requireContext());
            spacer.setLayoutParams(new android.widget.LinearLayout.LayoutParams(1, 20));
            layout.addView(spacer);

            android.widget.Button btnClockIn = new android.widget.Button(requireContext());
            btnClockIn.setText("B: Staff Clock-In");
            btnClockIn.setAllCaps(false);
            btnClockIn.setOnClickListener(v2 -> {
                showClockInDialog();
            });
            layout.addView(btnClockIn);

            builder.setView(layout);
            builder.setNegativeButton("Exit Kiosk", null);
            final android.app.AlertDialog entryDialog = builder.show();
            
            // Auto-close when sub-dialogs open
            btnRegister.setOnClickListener(v1 -> { showOnboardingDialog(); entryDialog.dismiss(); });
            btnClockIn.setOnClickListener(v2 -> { showClockInDialog(); entryDialog.dismiss(); });
        });

        // Hide FAB as it's now integrated in Kiosk
        view.findViewById(R.id.fab_add_employee).setVisibility(View.GONE);
    }

    private void showClockInDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setTitle("Staff Validation & Clock-In");
        
        android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(60, 20, 60, 20);

        final android.widget.EditText inputName = new android.widget.EditText(requireContext());
        inputName.setHint("Full Name (Exact)");
        layout.addView(inputName);

        final android.widget.EditText inputPin = new android.widget.EditText(requireContext());
        inputPin.setHint("4-Digit PIN");
        inputPin.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        layout.addView(inputPin);
        
        builder.setView(layout);
        builder.setPositiveButton("Verify & Log", (dialog, which) -> {
            String name = inputName.getText().toString();
            String pin = inputPin.getText().toString();
            if (!name.isEmpty() && pin.length() == 4) {
                viewModel.punchIn(name, pin, System.currentTimeMillis(), 
                    new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(new java.util.Date()));
                android.widget.Toast.makeText(getContext(), "Checking Database for " + name + "...", android.widget.Toast.LENGTH_SHORT).show();
            } else {
                android.widget.Toast.makeText(getContext(), "Please provide correct Name and 4-digit PIN", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Back", null);
        builder.show();
    }

    private void showOnboardingDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
        builder.setTitle("New Hire Registration");
        
        android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(60, 20, 60, 20);
        
        final android.widget.EditText inName = new android.widget.EditText(requireContext());
        inName.setHint("Full Name");
        layout.addView(inName);
        
        final android.widget.EditText inRole = new android.widget.EditText(requireContext());
        inRole.setHint("Role (Manager, Associate, etc)");
        layout.addView(inRole);
        
        final android.widget.EditText inRate = new android.widget.EditText(requireContext());
        inRate.setHint("Hourly Pay ($)");
        inRate.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layout.addView(inRate);
        
        final android.widget.EditText inPin = new android.widget.EditText(requireContext());
        inPin.setHint("Secure PIN (4 digits)");
        inPin.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(inPin);

        builder.setView(layout);
        builder.setPositiveButton("Complete Onboarding", (dialog, which) -> {
            if (!inName.getText().toString().isEmpty() && !inPin.getText().toString().isEmpty()) {
                com.abs.app.data.local.entity.EmployeeEntity newEmp = new com.abs.app.data.local.entity.EmployeeEntity();
                newEmp.name = inName.getText().toString();
                newEmp.role = inRole.getText().toString();
                newEmp.hourlyRate = Double.parseDouble(inRate.getText().toString().isEmpty() ? "15.0" : inRate.getText().toString());
                newEmp.securePin = inPin.getText().toString();
                viewModel.insertEmployee(newEmp);
                android.widget.Toast.makeText(getContext(), "Staff Registered!", android.widget.Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Back", null);
        builder.show();
    }
}
