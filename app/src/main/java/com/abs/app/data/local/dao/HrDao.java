package com.abs.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.abs.app.data.local.entity.AttendanceEntity;
import com.abs.app.data.local.entity.EmployeeEntity;

import java.util.List;

@Dao
public interface HrDao {
    @Insert
    long insertEmployee(EmployeeEntity employee);

    @Query("SELECT * FROM employees ORDER BY name ASC")
    LiveData<List<EmployeeEntity>> getAllEmployees();

    @Query("SELECT * FROM employees WHERE name = :name AND securePin = :pin LIMIT 1")
    EmployeeEntity getEmployeeByNameAndPin(String name, String pin);
    
    @Insert
    long insertAttendance(AttendanceEntity attendance);

    @Update
    void updateAttendance(AttendanceEntity attendance);

    @androidx.room.Delete
    void deleteEmployee(EmployeeEntity employee);

    @Query("SELECT * FROM attendances WHERE employeeId = :empId AND dateString = :date LIMIT 1")
    AttendanceEntity getAttendanceToday(int empId, String date);
}
