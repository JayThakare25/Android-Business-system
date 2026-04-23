package com.abs.app.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "attendances")
public class AttendanceEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public int employeeId;
    public long checkInMillis;
    public long checkOutMillis; // 0 if not checked out
    public String dateString; // YYYY-MM-DD
}
