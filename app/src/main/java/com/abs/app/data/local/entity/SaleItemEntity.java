package com.abs.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sale_items")
public class SaleItemEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public int saleId;
    public int productId;
    
    public int quantity;
    public double unitPrice;
    public double totalPrice;
}
