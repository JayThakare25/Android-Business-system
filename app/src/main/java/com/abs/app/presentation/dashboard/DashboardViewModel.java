package com.abs.app.presentation.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.abs.app.data.local.dao.SalesDao;
import com.abs.app.data.local.dao.InventoryDao;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class DashboardViewModel extends ViewModel {
    private final com.abs.app.data.local.dao.SalesDao salesDao;
    private final com.abs.app.data.local.dao.InventoryDao inventoryDao;
    private final com.abs.app.data.local.dao.FinanceDao financeDao;
    private final androidx.lifecycle.MutableLiveData<Long> timeframeStart = new androidx.lifecycle.MutableLiveData<>(System.currentTimeMillis() - (7 * 24 * 60 * 60 * 1000L));

    @Inject
    public DashboardViewModel(SalesDao salesDao, InventoryDao inventoryDao, com.abs.app.data.local.dao.FinanceDao financeDao) {
        this.salesDao = salesDao;
        this.inventoryDao = inventoryDao;
        this.financeDao = financeDao;
    }

    public void setTimeframe(int days) {
        timeframeStart.setValue(System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000L));
    }

    public LiveData<Double> getTodayRevenue(long startOfDay, long endOfDay) {
        return salesDao.getTodayRevenue(startOfDay, endOfDay);
    }

    public LiveData<Integer> getTotalSalesCount() {
        return salesDao.getTotalSalesCount();
    }

    public LiveData<java.util.List<com.abs.app.data.local.entity.ProductEntity>> getLowStockProducts() {
        return inventoryDao.getLowStockProducts();
    }

    public LiveData<java.util.List<com.abs.app.data.local.entity.SaleEntity>> getSalesForTimeframe() {
        return androidx.lifecycle.Transformations.switchMap(timeframeStart, start -> 
            salesDao.getSalesLast7Days(start) // Using existing query which takes a start millis
        );
    }

    public LiveData<java.util.List<com.abs.app.data.local.entity.ExpenseEntity>> getExpensesForTimeframe() {
        return androidx.lifecycle.Transformations.switchMap(timeframeStart, start -> 
            financeDao.getAllExpenses() // We'll filter these in memory or improve DAO later
        );
    }
}
