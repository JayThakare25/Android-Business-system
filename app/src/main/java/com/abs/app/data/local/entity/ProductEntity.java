package com.abs.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class ProductEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String name;
    public String skuBarcode;
    public String description;
    public String category;
    
    public double costPrice;
    public double sellingPrice;
    
    public int stockQuantity;
    public int reorderThreshold;
    
    public String imagePath;
    public String supplierId;
}
