package com.abs.app.presentation.crm;

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
public class CrmFragment extends Fragment {
    private CrmViewModel viewModel;
    private CrmAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_crm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CrmViewModel.class);

        view.findViewById(R.id.btn_back).setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });
        
        RecyclerView rv = view.findViewById(R.id.rv_customers);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new CrmAdapter();
        rv.setAdapter(adapter);

        viewModel.getAllCustomers().observe(getViewLifecycleOwner(), customers -> {
            if (customers != null) adapter.submitList(customers);
        });

        view.findViewById(R.id.fab_add_customer).setOnClickListener(v -> {
            com.abs.app.data.local.entity.CustomerEntity newCust = new com.abs.app.data.local.entity.CustomerEntity();
            newCust.name = "Demo Walk-in " + System.currentTimeMillis() % 1000;
            newCust.phoneNumber = "555-" + (1000 + (System.currentTimeMillis() % 8999));
            newCust.loyaltyPoints = 50;
            viewModel.insertCustomer(newCust);
            android.widget.Toast.makeText(getContext(), "Mock Customer Added!", android.widget.Toast.LENGTH_SHORT).show();
        });
    }
}
