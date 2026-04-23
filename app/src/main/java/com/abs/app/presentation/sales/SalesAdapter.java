package com.abs.app.presentation.sales;

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

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SalesViewHolder> {

    public interface OnPosItemInteractionListener {
        void onItemClick(ProductEntity product);
        void onItemLongClick(ProductEntity product);
    }

    private List<ProductEntity> productList = new ArrayList<>();
    private OnPosItemInteractionListener interactionListener;

    public void setOnPosItemInteractionListener(OnPosItemInteractionListener listener) {
        this.interactionListener = listener;
    }

    public void submitList(List<ProductEntity> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_pos, parent, false);
        return new SalesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SalesViewHolder holder, int position) {
        ProductEntity product = productList.get(position);
        holder.tvName.setText(product.name);
        holder.tvPrice.setText(String.format("$%.2f", product.sellingPrice));
        holder.tvStock.setText("Stock: " + product.stockQuantity);
        
        holder.itemView.setOnClickListener(v -> {
            if (interactionListener != null) interactionListener.onItemClick(product);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (interactionListener != null) interactionListener.onItemLongClick(product);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class SalesViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvStock;

        public SalesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_pos_product_name);
            tvPrice = itemView.findViewById(R.id.tv_pos_product_price);
            tvStock = itemView.findViewById(R.id.tv_pos_product_stock);
        }
    }
}
