package com.abs.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.abs.app.data.local.entity.ExpenseEntity;
import com.abs.app.data.local.entity.InvoiceEntity;

import java.util.List;

@Dao
public interface FinanceDao {
    @Insert
    long insertExpense(ExpenseEntity expense);

    @Query("SELECT * FROM expenses ORDER BY timestampMillis DESC")
    LiveData<List<ExpenseEntity>> getAllExpenses();

    @Query("SELECT SUM(amount) FROM expenses WHERE timestampMillis >= :startMillis AND timestampMillis <= :endMillis")
    LiveData<Double> getTotalExpensesPeriod(long startMillis, long endMillis);

    @Insert
    long insertInvoice(InvoiceEntity invoice);

    @Query("SELECT * FROM invoices ORDER BY dueDateMillis ASC")
    LiveData<List<InvoiceEntity>> getAllInvoices();
}
