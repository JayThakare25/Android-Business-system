package com.abs.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.abs.app.data.local.entity.CustomerEntity;

import java.util.List;

@Dao
public interface CrmDao {
    @Insert
    long insertCustomer(CustomerEntity customer);

    @Update
    void updateCustomer(CustomerEntity customer);

    @Query("SELECT * FROM customers ORDER BY name ASC")
    LiveData<List<CustomerEntity>> getAllCustomers();

    @Query("UPDATE customers SET loyaltyPoints = loyaltyPoints + :points WHERE id = :customerId")
    void addLoyaltyPoints(int customerId, int points);
}
