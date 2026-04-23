package com.abs.app.data.local;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.abs.app.data.local.entity.ProductEntity;
import com.abs.app.data.local.entity.CustomerEntity;
import com.abs.app.data.local.entity.EmployeeEntity;
import com.abs.app.data.local.entity.ExpenseEntity;

@Singleton
public class DemoDataSeeder {
    private final ABSDatabase database;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Inject
    public DemoDataSeeder(ABSDatabase database) {
        this.database = database;
    }

    public void seedDatabaseIfEmpty() {
        executor.execute(() -> {
            try {
                if (database.inventoryDao().getProductById(1) == null) {
                    // 1. Seed Products
                    database.inventoryDao().insertProduct(createProduct("Quantum Laptop X1", "LAP-X1-001", 1200.00, 15, 5, "Electronics"));
                    database.inventoryDao().insertProduct(createProduct("Mechanical Keyboard V3", "KEY-V3-002", 150.00, 40, 10, "Peripherals"));
                    database.inventoryDao().insertProduct(createProduct("Ultra HD Monitor 32\"", "MON-32-003", 450.00, 8, 2, "Electronics"));
                    database.inventoryDao().insertProduct(createProduct("Wireless Laser Mouse", "MOU-WL-004", 45.00, 60, 15, "Peripherals"));
                    database.inventoryDao().insertProduct(createProduct("Ergonomic Mesh Chair", "CHR-EM-005", 280.00, 20, 5, "Furniture"));

                    // 2. Seed Customers
                    database.crmDao().insertCustomer(createCustomer("Acme Corp", "contact@acme.com", "555-0101", 5000.00, "Enterprise Customer"));
                    database.crmDao().insertCustomer(createCustomer("Globex Inc", "admin@globex.com", "555-0202", 1200.00, "Retails"));
                    database.crmDao().insertCustomer(createCustomer("Stark Industries", "tony@stark.com", "555-0303", 10000.00, "VIP Account"));
                    database.crmDao().insertCustomer(createCustomer("Wayne Enterprises", "bruce@wayne.com", "555-0404", 8500.00, "VIP Account"));

                    // 3. Seed Employees
                    database.hrDao().insertEmployee(createEmployee("Alice Smith", "Store Manager", "alice@abs.com", "555-1001", 65000.0));
                    database.hrDao().insertEmployee(createEmployee("Bob Jones", "Sales Associate", "bob@abs.com", "555-1002", 45000.0));
                    database.hrDao().insertEmployee(createEmployee("Charlie Brown", "Inventory Clerk", "charlie@abs.com", "555-1003", 42000.0));
                    database.hrDao().insertEmployee(createEmployee("Diana Prince", "HR Director", "diana@abs.com", "555-1004", 75000.0));

                    // 4. Seed Finances (Expenses)
                    database.financeDao().insertExpense(createExpense("Store Rent - May", 2500.00, "Utilities", System.currentTimeMillis() - 86400000L));
                    database.financeDao().insertExpense(createExpense("Inventory Restock - Keyboards", 1200.00, "COGS", System.currentTimeMillis() - 172800000L));
                    database.financeDao().insertExpense(createExpense("Marketing Google Ads", 450.00, "Marketing", System.currentTimeMillis() - 259200000L));
                    database.financeDao().insertExpense(createExpense("Office Supplies", 150.00, "Operations", System.currentTimeMillis()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private ProductEntity createProduct(String name, String sku, double price, int stock, int reorder, String category) {
        ProductEntity p = new ProductEntity();
        p.name = name;
        p.skuBarcode = sku;
        p.sellingPrice = price;
        p.costPrice = price * 0.6;
        p.stockQuantity = stock;
        p.reorderThreshold = reorder;
        p.category = category;
        return p;
    }

    private CustomerEntity createCustomer(String name, String email, String phone, double revenue, String notes) {
        CustomerEntity c = new CustomerEntity();
        c.name = name;
        c.email = email;
        c.phoneNumber = phone;
        c.loyaltyPoints = (int) (revenue / 10);
        c.notes = notes;
        return c;
    }

    private EmployeeEntity createEmployee(String name, String role, String email, String phone, double salary) {
        EmployeeEntity e = new EmployeeEntity();
        e.name = name;
        e.role = role;
        e.hourlyRate = salary / 2000.0;
        e.securePin = "1234";
        return e;
    }

    private ExpenseEntity createExpense(String desc, double amount, String category, long timestamp) {
        ExpenseEntity e = new ExpenseEntity();
        e.description = desc;
        e.amount = amount;
        e.category = category;
        e.timestampMillis = timestamp;
        return e;
    }
}
