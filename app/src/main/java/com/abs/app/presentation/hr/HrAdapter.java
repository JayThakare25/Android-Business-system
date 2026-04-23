package com.abs.app.presentation.hr;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.abs.app.R;
import com.abs.app.data.local.entity.EmployeeEntity;
import java.util.ArrayList;
import java.util.List;

public class HrAdapter extends RecyclerView.Adapter<HrAdapter.EmpViewHolder> {
    public interface OnEmployeeDeleteListener {
        void onDeleteClick(EmployeeEntity employee);
    }

    private List<EmployeeEntity> list = new ArrayList<>();
    private OnEmployeeDeleteListener deleteListener;

    public void setOnEmployeeDeleteListener(OnEmployeeDeleteListener listener) {
        this.deleteListener = listener;
    }

    public void submitList(List<EmployeeEntity> newList) {
        list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EmpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new EmpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpViewHolder holder, int position) {
        EmployeeEntity e = list.get(position);
        holder.name.setText(e.name);
        holder.role.setText(e.role);
        holder.rate.setText(String.format("Rate: $%.2f/hr", e.hourlyRate));
        
        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) deleteListener.onDeleteClick(e);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class EmpViewHolder extends RecyclerView.ViewHolder {
        TextView name, role, rate;
        android.widget.ImageButton btnDelete;

        public EmpViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_emp_name);
            role = itemView.findViewById(R.id.tv_emp_role);
            rate = itemView.findViewById(R.id.tv_emp_rate);
            btnDelete = itemView.findViewById(R.id.btn_delete_employee);
        }
    }
}
