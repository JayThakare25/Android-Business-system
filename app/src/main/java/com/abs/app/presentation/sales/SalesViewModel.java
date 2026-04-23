package com.abs.app.presentation.sales;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.abs.app.data.local.dao.InventoryDao;
import com.abs.app.data.local.entity.ProductEntity;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SalesViewModel extends ViewModel {
    private final InventoryDao inventoryDao;
    private final com.abs.app.data.local.dao.SalesDao salesDao;

    @Inject
    public SalesViewModel(InventoryDao inventoryDao, com.abs.app.data.local.dao.SalesDao salesDao) {
        this.inventoryDao = inventoryDao;
        this.salesDao = salesDao;
    }

    public LiveData<List<ProductEntity>> getAvailableProducts() {
        return inventoryDao.getProductsInStock();
    }

    public void deductStock(int productId, int quantity) {
        new Thread(() -> inventoryDao.deductStock(productId, quantity)).start();
    }

    public void insertSale(com.abs.app.data.local.entity.SaleEntity sale) {
        new Thread(() -> salesDao.insertSale(sale)).start();
    }
}
