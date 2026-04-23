package com.abs.app.presentation.sales;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.abs.app.R;
import java.util.ArrayList;
import java.util.List;

public class CartPreviewAdapter extends RecyclerView.Adapter<CartPreviewAdapter.CartPreviewViewHolder> {

    public static class CartDisplayItem {
        public String name;
        public int qty;
        public CartDisplayItem(String name, int qty) { this.name = name; this.qty = qty; }
    }

    private List<CartDisplayItem> cartItems = new ArrayList<>();

    public void submitList(List<CartDisplayItem> items) {
        this.cartItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartPreviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_mini, parent, false);
        return new CartPreviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartPreviewViewHolder holder, int position) {
        CartDisplayItem item = cartItems.get(position);
        holder.tvName.setText(item.name);
        holder.tvQty.setText(String.valueOf(item.qty));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartPreviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQty;
        public CartPreviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_mini_name);
            tvQty = itemView.findViewById(R.id.tv_mini_qty);
        }
    }
}
