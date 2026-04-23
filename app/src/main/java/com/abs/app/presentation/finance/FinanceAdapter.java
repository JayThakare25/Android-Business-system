package com.abs.app.presentation.finance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.abs.app.R;
import com.abs.app.data.local.entity.ExpenseEntity;
import java.util.ArrayList;
import java.util.List;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.FinViewHolder> {
    private List<ExpenseEntity> list = new ArrayList<>();

    public void submitList(List<ExpenseEntity> newList) {
        list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new FinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinViewHolder holder, int position) {
        ExpenseEntity e = list.get(position);
        holder.desc.setText(e.category + " - " + e.description);
        holder.amt.setText(String.format("-$%.2f", e.amount));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class FinViewHolder extends RecyclerView.ViewHolder {
        TextView desc, amt;
        public FinViewHolder(@NonNull View itemView) {
            super(itemView);
            desc = itemView.findViewById(R.id.tv_tran_desc);
            amt = itemView.findViewById(R.id.tv_tran_amt);
        }
    }
}
