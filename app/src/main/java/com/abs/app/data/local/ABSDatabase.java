package com.abs.app.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.abs.app.data.local.dao.InventoryDao;
import com.abs.app.data.local.dao.SalesDao;
import com.abs.app.data.local.dao.FinanceDao;
import com.abs.app.data.local.dao.HrDao;
import com.abs.app.data.local.dao.CrmDao;
import com.abs.app.data.local.entity.ProductEntity;
import com.abs.app.data.local.entity.SaleEntity;
import com.abs.app.data.local.entity.SaleItemEntity;
import com.abs.app.data.local.entity.ExpenseEntity;
import com.abs.app.data.local.entity.InvoiceEntity;
import com.abs.app.data.local.entity.EmployeeEntity;
import com.abs.app.data.local.entity.AttendanceEntity;
import com.abs.app.data.local.entity.CustomerEntity;

@Database(
    entities = {
        ProductEntity.class, 
        SaleEntity.class, 
        SaleItemEntity.class,
        ExpenseEntity.class,
        InvoiceEntity.class,
        EmployeeEntity.class,
        AttendanceEntity.class,
        CustomerEntity.class
    }, 
    version = 3, 
    exportSchema = false
)
public abstract class ABSDatabase extends RoomDatabase {
    public abstract InventoryDao inventoryDao();
    public abstract SalesDao salesDao();
    public abstract FinanceDao financeDao();
    public abstract HrDao hrDao();
    public abstract CrmDao crmDao();
}
