package com.abs.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "customers")
public class CustomerEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String name;
    public String phoneNumber;
    public String email;
    public int loyaltyPoints;
    public String notes;
}
