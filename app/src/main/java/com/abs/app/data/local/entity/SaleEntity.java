package com.abs.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sales")
public class SaleEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public long timestampMillis;
    public double subtotal;
    public double discountApplied;
    public double totalAmount;
    
    public String paymentMethod; // CASH, CARD, MOBILE
    public int customerId; // CRM linkage tracking
}
