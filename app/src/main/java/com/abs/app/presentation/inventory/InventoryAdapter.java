package com.abs.app.presentation.inventory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.abs.app.R;
import com.abs.app.data.local.entity.ProductEntity;
import java.util.ArrayList;
import java.util.List;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.ProductViewHolder> {

    public interface OnProductDeleteListener {
        void onDeleteClick(ProductEntity product);
    }

    public interface OnProductRestockListener {
        void onRestockClick(ProductEntity product);
    }

    private List<ProductEntity> productList = new ArrayList<>();
    private OnProductDeleteListener deleteListener;
    private OnProductRestockListener restockListener;

    public void setOnProductDeleteListener(OnProductDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void setOnProductRestockListener(OnProductRestockListener listener) {
        this.restockListener = listener;
    }

    public void submitList(List<ProductEntity> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_inventory, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductEntity product = productList.get(position);
        holder.tvName.setText(product.name);
        holder.tvPrice.setText(String.format("$%.2f", product.sellingPrice));
        holder.tvStock.setText("Stock: " + product.stockQuantity);
        
        if (product.stockQuantity == 0) {
            holder.tvLowStock.setText("OUT OF STOCK");
            holder.tvLowStock.setTextColor(android.graphics.Color.RED);
            holder.tvLowStock.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(0.6f); // Visually "disabled"
        } else if (product.stockQuantity <= product.reorderThreshold) {
            holder.tvLowStock.setText("LOW STOCK");
            holder.tvLowStock.setTextColor(android.graphics.Color.parseColor("#FF8C00")); // Dark Orange
            holder.tvLowStock.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(1.0f);
        } else {
            holder.tvLowStock.setVisibility(View.GONE);
            holder.itemView.setAlpha(1.0f);
        }

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(product);
            }
        });

        holder.btnRestock.setOnClickListener(v -> {
            if (restockListener != null) {
                restockListener.onRestockClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvStock, tvLowStock;
        android.widget.ImageButton btnDelete, btnRestock;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_product_name);
            tvPrice = itemView.findViewById(R.id.tv_product_price);
            tvStock = itemView.findViewById(R.id.tv_product_stock);
            tvLowStock = itemView.findViewById(R.id.tv_low_stock_warning);
            btnDelete = itemView.findViewById(R.id.btn_delete_product);
            btnRestock = itemView.findViewById(R.id.btn_restock_product);
        }
    }
}
