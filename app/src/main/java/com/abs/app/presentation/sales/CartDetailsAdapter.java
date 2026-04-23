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

public class CartDetailsAdapter extends RecyclerView.Adapter<CartDetailsAdapter.CartItemViewHolder> {

    public interface OnCartItemRemovalListener {
        void onRemoveItem(ProductEntity product);
    }

    public static class CartDisplayModel {
        public ProductEntity product;
        public int qty;
        public CartDisplayModel(ProductEntity product, int qty) { this.product = product; this.qty = qty; }
    }

    private List<CartDisplayModel> items = new ArrayList<>();
    private OnCartItemRemovalListener removalListener;

    public void setOnCartItemRemovalListener(OnCartItemRemovalListener listener) {
        this.removalListener = listener;
    }

    public void submitList(List<CartDisplayModel> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_full, parent, false);
        return new CartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        CartDisplayModel model = items.get(position);
        holder.tvName.setText(model.product.name);
        holder.tvDetails.setText(model.qty + " x " + String.format("$%.2f", model.product.sellingPrice));
        holder.tvTotal.setText(String.format("$%.2f", model.product.sellingPrice * model.qty));
        
        holder.btnRemove.setOnClickListener(v -> {
            if (removalListener != null) removalListener.onRemoveItem(model.product);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class CartItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails, tvTotal;
        android.widget.ImageButton btnRemove;

        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_cart_item_name);
            tvDetails = itemView.findViewById(R.id.tv_cart_item_details);
            tvTotal = itemView.findViewById(R.id.tv_cart_item_total);
            btnRemove = itemView.findViewById(R.id.btn_remove_item);
        }
    }
}
