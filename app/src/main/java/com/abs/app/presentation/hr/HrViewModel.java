package com.abs.app.presentation.hr;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.abs.app.data.local.dao.HrDao;
import com.abs.app.data.local.entity.AttendanceEntity;
import com.abs.app.data.local.entity.EmployeeEntity;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HrViewModel extends ViewModel {
    private final HrDao hrDao;

    @Inject
    public HrViewModel(HrDao hrDao) {
        this.hrDao = hrDao;
    }

    private final androidx.lifecycle.MutableLiveData<String> clockInStatus = new androidx.lifecycle.MutableLiveData<>();

    public androidx.lifecycle.LiveData<String> getClockInStatus() {
        return clockInStatus;
    }

    public LiveData<List<EmployeeEntity>> getAllEmployees() {
        return hrDao.getAllEmployees();
    }

    public void insertEmployee(EmployeeEntity employee) {
        new Thread(() -> hrDao.insertEmployee(employee)).start();
    }

    public void deleteEmployee(EmployeeEntity employee) {
        new Thread(() -> hrDao.deleteEmployee(employee)).start();
    }

    public void punchIn(String name, String pin, long timeMillis, String dateStr) {
        new Thread(() -> {
            EmployeeEntity emp = hrDao.getEmployeeByNameAndPin(name, pin);
            if (emp != null) {
                AttendanceEntity log = new AttendanceEntity();
                log.employeeId = emp.id;
                log.checkInMillis = timeMillis;
                log.dateString = dateStr;
                hrDao.insertAttendance(log);
                clockInStatus.postValue("Attendance Recorded: " + emp.name);
            } else {
                clockInStatus.postValue("Error: Employee not found or PIN incorrect.");
            }
        }).start();
    }
}
