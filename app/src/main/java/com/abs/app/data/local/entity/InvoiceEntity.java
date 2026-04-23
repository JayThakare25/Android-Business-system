package com.abs.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "invoices")
public class InvoiceEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String clientName;
    public double amountDue;
    public long dueDateMillis;
    public String status; // PENDING, PAID, OVERDUE
}
