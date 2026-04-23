package com.abs.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.abs.app.data.local.entity.SaleEntity;
import com.abs.app.data.local.entity.SaleItemEntity;

import java.util.List;

@Dao
public interface SalesDao {

    @Insert
    long insertSale(SaleEntity sale);

    @Insert
    void insertSaleItems(List<SaleItemEntity> items);

    @Transaction
    @Query("SELECT * FROM sales ORDER BY timestampMillis DESC")
    LiveData<List<SaleEntity>> getAllSales();

    @Query("SELECT SUM(totalAmount) FROM sales WHERE timestampMillis >= :startOfDayMillis AND timestampMillis <= :endOfDayMillis")
    LiveData<Double> getTodayRevenue(long startOfDayMillis, long endOfDayMillis);

    @Query("SELECT SUM(totalAmount) FROM sales")
    LiveData<Double> getTotalRevenue();
    
    @Query("SELECT COUNT(*) FROM sales")
    LiveData<Integer> getTotalSalesCount();
    
    // For Dashboard MPAndroidChart
    @Query("SELECT * FROM sales WHERE timestampMillis >= :sevenDaysAgoMillis ORDER BY timestampMillis ASC")
    LiveData<List<SaleEntity>> getSalesLast7Days(long sevenDaysAgoMillis);
}
