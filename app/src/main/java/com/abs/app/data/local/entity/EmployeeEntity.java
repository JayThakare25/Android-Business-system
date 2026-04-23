package com.abs.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "employees")
public class EmployeeEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String name;
    public String role; // Cashier, Manager, Admin
    public double hourlyRate;
    public String securePin;
    public String qrCodeHash;
}
