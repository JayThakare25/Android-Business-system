package com.abs.app.presentation.crm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.abs.app.data.local.dao.CrmDao;
import com.abs.app.data.local.entity.CustomerEntity;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CrmViewModel extends ViewModel {
    private final CrmDao crmDao;

    @Inject
    public CrmViewModel(CrmDao crmDao) {
        this.crmDao = crmDao;
    }

    public LiveData<List<CustomerEntity>> getAllCustomers() {
        return crmDao.getAllCustomers();
    }

    public void insertCustomer(CustomerEntity customer) {
        new Thread(() -> crmDao.insertCustomer(customer)).start();
    }
}
