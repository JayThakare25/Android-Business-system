package com.abs.app.presentation.inventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.abs.app.R;
import dagger.hilt.android.AndroidEntryPoint;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@AndroidEntryPoint
public class InventoryFragment extends Fragment {
    
    private InventoryViewModel viewModel;
    private InventoryAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(InventoryViewModel.class);
        
        RecyclerView rv = view.findViewById(R.id.rv_inventory);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new InventoryAdapter();
        adapter.setOnProductDeleteListener(product -> {
            viewModel.deleteProduct(product);
            android.widget.Toast.makeText(getContext(), product.name + " deleted", android.widget.Toast.LENGTH_SHORT).show();
        });
        
        adapter.setOnProductRestockListener(product -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
            builder.setTitle("Restock " + product.name);
            
            final android.widget.EditText input = new android.widget.EditText(requireContext());
            input.setHint("Quantity to add (e.g. 20)");
            input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            
            android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
            layout.setPadding(50, 20, 50, 20);
            layout.addView(input);
            builder.setView(layout);
            
            builder.setPositiveButton("Add Stock", (dialog, which) -> {
                String qtyStr = input.getText().toString();
                if (!qtyStr.isEmpty()) {
                    int qty = Integer.parseInt(qtyStr);
                    viewModel.restockProduct(product.id, qty);
                    android.widget.Toast.makeText(getContext(), "Added " + qty + " units to " + product.name, android.widget.Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", null);
            builder.show();
        });

        rv.setAdapter(adapter);

        viewModel.getAllProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) adapter.submitList(products);
        });

        view.findViewById(R.id.fab_add_product).setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
            builder.setTitle("Add New Inventory Item");

            android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);
            layout.setPadding(50, 20, 50, 20);

            final android.widget.EditText inputName = new android.widget.EditText(requireContext());
            inputName.setHint("Item Name (e.g. Printer)");
            layout.addView(inputName);

            final android.widget.EditText inputPrice = new android.widget.EditText(requireContext());
            inputPrice.setHint("Price (e.g. 199.99)");
            inputPrice.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
            layout.addView(inputPrice);

            final android.widget.EditText inputStock = new android.widget.EditText(requireContext());
            inputStock.setHint("Stock Quantity (e.g. 50)");
            inputStock.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            layout.addView(inputStock);

            builder.setView(layout);

            builder.setPositiveButton("Add", (dialog, which) -> {
                String name = inputName.getText().toString();
                String priceStr = inputPrice.getText().toString();
                String stockStr = inputStock.getText().toString();
                
                if(!name.isEmpty() && !priceStr.isEmpty() && !stockStr.isEmpty()) {
                    com.abs.app.data.local.entity.ProductEntity newProd = new com.abs.app.data.local.entity.ProductEntity();
                    newProd.name = name;
                    newProd.skuBarcode = "CUSTOM-" + System.currentTimeMillis();
                    newProd.sellingPrice = Double.parseDouble(priceStr);
                    newProd.costPrice = newProd.sellingPrice * 0.6;
                    newProd.stockQuantity = Integer.parseInt(stockStr);
                    newProd.reorderThreshold = 10;
                    newProd.category = "Custom";
                    viewModel.insertProduct(newProd);
                    android.widget.Toast.makeText(getContext(), name + " Added!", android.widget.Toast.LENGTH_SHORT).show();
                } else {
                    android.widget.Toast.makeText(getContext(), "Fields cannot be empty", android.widget.Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });
    }
}
