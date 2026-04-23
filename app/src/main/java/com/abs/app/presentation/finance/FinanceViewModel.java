package com.abs.app.presentation.finance;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.abs.app.data.local.dao.FinanceDao;
import com.abs.app.data.local.entity.ExpenseEntity;
import com.abs.app.data.local.entity.InvoiceEntity;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class FinanceViewModel extends ViewModel {
    private final FinanceDao financeDao;
    private final com.abs.app.data.local.dao.SalesDao salesDao;

    @Inject
    public FinanceViewModel(FinanceDao financeDao, com.abs.app.data.local.dao.SalesDao salesDao) {
        this.financeDao = financeDao;
        this.salesDao = salesDao;
    }

    public LiveData<Double> getTotalRevenue() {
        return salesDao.getTotalRevenue();
    }

    public LiveData<List<ExpenseEntity>> getAllExpenses() {
        return financeDao.getAllExpenses();
    }

    public LiveData<List<InvoiceEntity>> getAllInvoices() {
        return financeDao.getAllInvoices();
    }

    public void insertExpense(ExpenseEntity expense) {
        new Thread(() -> financeDao.insertExpense(expense)).start();
    }

    public void insertInvoice(InvoiceEntity invoice) {
        new Thread(() -> financeDao.insertInvoice(invoice)).start();
    }
}
