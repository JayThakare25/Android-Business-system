package com.abs.app.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;

import com.abs.app.data.local.entity.ProductEntity;

import java.util.List;

@Dao
public interface InventoryDao {
    @Query("SELECT * FROM products ORDER BY name ASC")
    LiveData<List<ProductEntity>> getAllProducts();

    @Query("SELECT * FROM products WHERE stockQuantity > 0 ORDER BY name ASC")
    LiveData<List<ProductEntity>> getProductsInStock();

    @Query("SELECT * FROM products WHERE stockQuantity <= reorderThreshold")
    LiveData<List<ProductEntity>> getLowStockProducts();

    @Query("SELECT * FROM products WHERE skuBarcode = :barcode LIMIT 1")
    ProductEntity getProductByBarcode(String barcode);
    
    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    ProductEntity getProductById(int id);

    @Query("SELECT COUNT(*) FROM products WHERE stockQuantity <= reorderThreshold")
    LiveData<Integer> getPendingLowStockCount();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProduct(ProductEntity product);

    @Update
    void updateProduct(ProductEntity product);
    
    @Query("UPDATE products SET stockQuantity = stockQuantity - :quantity WHERE id = :productId")
    void deductStock(int productId, int quantity);

    @Query("UPDATE products SET stockQuantity = stockQuantity + :quantity WHERE id = :productId")
    void restockStock(int productId, int quantity);
    
    @Delete
    void deleteProduct(ProductEntity product);
}
