package com.abs.app.presentation.sales;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@AndroidEntryPoint
public class SalesFragment extends Fragment {

    private SalesViewModel viewModel;
    private SalesAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sales, container, false);
    }

    private static class CartItem {
        com.abs.app.data.local.entity.ProductEntity product;
        int qty;
        CartItem(com.abs.app.data.local.entity.ProductEntity product, int qty) { this.product = product; this.qty = qty; }
    }
    private java.util.Map<Integer, CartItem> activeCart = new java.util.HashMap<>();

    private CartDetailsAdapter cartDetailsAdapter;
    private com.google.android.material.bottomsheet.BottomSheetBehavior<View> bottomSheetBehavior;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(SalesViewModel.class);
        
        android.widget.TextView tvCartCount = view.findViewById(R.id.tv_cart_items_count);
        android.widget.Button btnCheckout = view.findViewById(R.id.btn_checkout);
        
        // Setup Bottom Sheet
        View bottomSheet = view.findViewById(R.id.cart_bottom_sheet);
        bottomSheetBehavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED);
        
        View.OnClickListener toggleSheet = v -> {
            if (bottomSheetBehavior.getState() != com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.setState(com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED);
            } else {
                bottomSheetBehavior.setState(com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED);
            }
        };

        view.findViewById(R.id.summary_bar).setOnClickListener(toggleSheet);
        view.findViewById(R.id.sheet_handle_area).setOnClickListener(toggleSheet);

        RecyclerView rvCartDetails = view.findViewById(R.id.rv_cart_details);
        rvCartDetails.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(requireContext()));
        cartDetailsAdapter = new CartDetailsAdapter();
        cartDetailsAdapter.setOnCartItemRemovalListener(product -> {
            activeCart.remove(product.id);
            // Internal runnable update logic is captured in context below
            // This is handled by a direct method call in the actual implementation update
            updateCartUI(tvCartCount, btnCheckout); 
        });
        rvCartDetails.setAdapter(cartDetailsAdapter);

        RecyclerView rvProducts = view.findViewById(R.id.rv_pos_products);
        rvProducts.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        adapter = new SalesAdapter();
        adapter.setOnPosItemInteractionListener(new SalesAdapter.OnPosItemInteractionListener() {
            @Override
            public void onItemClick(com.abs.app.data.local.entity.ProductEntity product) {
                if (activeCart.containsKey(product.id)) {
                    activeCart.get(product.id).qty += 1;
                } else {
                    activeCart.put(product.id, new CartItem(product, 1));
                }
                updateCartUI(tvCartCount, btnCheckout);
                android.widget.Toast.makeText(getContext(), "Added 1 " + product.name, android.widget.Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(com.abs.app.data.local.entity.ProductEntity product) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(requireContext());
                builder.setTitle("Add Multiple to Cart");
                
                final android.widget.EditText inputQty = new android.widget.EditText(requireContext());
                inputQty.setHint("Quantity (e.g. 5)");
                inputQty.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                
                android.widget.LinearLayout layout = new android.widget.LinearLayout(requireContext());
                layout.setPadding(50, 20, 50, 20);
                layout.addView(inputQty);
                builder.setView(layout);
                
                builder.setPositiveButton("Add to Cart", (dialog, which) -> {
                    String qtyStr = inputQty.getText().toString();
                    if (!qtyStr.isEmpty()) {
                        int qty = Integer.parseInt(qtyStr);
                        int currentQty = activeCart.containsKey(product.id) ? activeCart.get(product.id).qty : 0;
                        
                        if ((currentQty + qty) > product.stockQuantity) {
                            android.widget.Toast.makeText(getContext(), "Cannot add! Max available in stock: " + (product.stockQuantity - currentQty), android.widget.Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (activeCart.containsKey(product.id)) {
                            activeCart.get(product.id).qty += qty;
                        } else {
                            activeCart.put(product.id, new CartItem(product, qty));
                        }
                        updateCartUI(tvCartCount, btnCheckout);
                        android.widget.Toast.makeText(getContext(), "Added " + qty + " " + product.name, android.widget.Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });
        rvProducts.setAdapter(adapter);

        viewModel.getAvailableProducts().observe(getViewLifecycleOwner(), products -> {
            if (products != null) adapter.submitList(products);
        });

        btnCheckout.setOnClickListener(v -> {
            if (activeCart.isEmpty()) {
                android.widget.Toast.makeText(getContext(), "Cart is empty", android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            StringBuilder receiptPreview = new StringBuilder("Items Checkout Receipt:\n\n");
            double calculateTotal = 0.0;
            for(CartItem item : activeCart.values()) {
                receiptPreview.append(item.qty).append("x ").append(item.product.name)
                              .append(" - $").append(String.format("%.2f", item.product.sellingPrice * item.qty))
                              .append("\n");
                calculateTotal += (item.product.sellingPrice * item.qty);
            }
            receiptPreview.append("\nTotal Expected: $").append(String.format("%.2f", calculateTotal));

            android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(requireContext());
            dialog.setTitle("Confirm Transaction");
            dialog.setMessage(receiptPreview.toString());
            dialog.setPositiveButton("Complete Checkout", (d, w) -> {
                double finalTotal = 0.0;
                for(CartItem item : activeCart.values()) {
                    viewModel.deductStock(item.product.id, item.qty);
                    finalTotal += (item.product.sellingPrice * item.qty);
                }
                
                // Record the sale in the database
                com.abs.app.data.local.entity.SaleEntity sale = new com.abs.app.data.local.entity.SaleEntity();
                sale.totalAmount = finalTotal;
                sale.timestampMillis = System.currentTimeMillis();
                sale.paymentMethod = "CASH"; // Demo default
                viewModel.insertSale(sale);

                activeCart.clear();
                updateCartUI(tvCartCount, btnCheckout);
                bottomSheetBehavior.setState(com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED);
                android.widget.Toast.makeText(getContext(), "Checkout Success! Revenue Recorded.", android.widget.Toast.LENGTH_LONG).show();
            });
            dialog.setNegativeButton("Modify Cart", null);
            dialog.show();
        });
    }

    private void updateCartUI(android.widget.TextView tvCount, android.widget.Button btn) {
        int totalItems = 0;
        double totalPrice = 0.0;
        java.util.List<CartDetailsAdapter.CartDisplayModel> list = new java.util.ArrayList<>();
        
        for(CartItem item : activeCart.values()) {
            totalItems += item.qty;
            totalPrice += (item.product.sellingPrice * item.qty);
            list.add(new CartDetailsAdapter.CartDisplayModel(item.product, item.qty));
        }
        
        cartDetailsAdapter.submitList(list);
        tvCount.setText(totalItems + " Items in Cart");
        btn.setText(String.format("Checkout $%.2f", totalPrice));
    }
}
