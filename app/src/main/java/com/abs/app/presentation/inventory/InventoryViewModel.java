package com.abs.app.presentation.inventory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.abs.app.data.local.dao.InventoryDao;
import com.abs.app.data.local.entity.ProductEntity;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class InventoryViewModel extends ViewModel {
    private final InventoryDao inventoryDao;

    @Inject
    public InventoryViewModel(InventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    public LiveData<List<ProductEntity>> getAllProducts() {
        return inventoryDao.getAllProducts();
    }

    public void insertProduct(ProductEntity product) {
        new Thread(() -> inventoryDao.insertProduct(product)).start();
    }

    public void deleteProduct(ProductEntity product) {
        new Thread(() -> inventoryDao.deleteProduct(product)).start();
    }

    public void restockProduct(int productId, int quantity) {
        new Thread(() -> inventoryDao.restockStock(productId, quantity)).start();
    }
}
