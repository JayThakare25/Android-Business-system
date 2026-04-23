package com.abs.app.presentation.crm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.abs.app.R;
import com.abs.app.data.local.entity.CustomerEntity;
import java.util.ArrayList;
import java.util.List;

public class CrmAdapter extends RecyclerView.Adapter<CrmAdapter.CustomerViewHolder> {
    private List<CustomerEntity> list = new ArrayList<>();

    public void submitList(List<CustomerEntity> newList) {
        list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        CustomerEntity c = list.get(position);
        holder.name.setText(c.name);
        holder.phone.setText(c.phoneNumber != null ? c.phoneNumber : "No Phone");
        holder.pts.setText("Loyalty Pts: " + c.loyaltyPoints);
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, pts;
        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_customer_name);
            phone = itemView.findViewById(R.id.tv_customer_phone);
            pts = itemView.findViewById(R.id.tv_loyalty_points);
        }
    }
}
