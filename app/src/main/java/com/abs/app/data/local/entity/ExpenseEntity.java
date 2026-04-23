package com.abs.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "expenses")
public class ExpenseEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public double amount;
    public String category; // Utilities, Restock, Rent, Misc
    public long timestampMillis;
    public String description;
}
