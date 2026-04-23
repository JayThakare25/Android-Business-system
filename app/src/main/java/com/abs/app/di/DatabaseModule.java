package com.abs.app.di;

import android.content.Context;
import androidx.room.Room;

import com.abs.app.data.local.ABSDatabase;
import com.abs.app.data.local.dao.InventoryDao;
import com.abs.app.data.local.dao.SalesDao;
import com.abs.app.data.local.dao.FinanceDao;
import com.abs.app.data.local.dao.HrDao;
import com.abs.app.data.local.dao.CrmDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public ABSDatabase provideABSDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(
                context, 
                ABSDatabase.class, 
                "abs_database"
        )
        .fallbackToDestructiveMigration()
        .build();
    }

    @Provides
    @Singleton
    public InventoryDao provideInventoryDao(ABSDatabase db) {
        return db.inventoryDao();
    }

    @Provides
    @Singleton
    public SalesDao provideSalesDao(ABSDatabase db) {
        return db.salesDao();
    }

    @Provides
    @Singleton
    public FinanceDao provideFinanceDao(ABSDatabase db) {
        return db.financeDao();
    }

    @Provides
    @Singleton
    public HrDao provideHrDao(ABSDatabase db) {
        return db.hrDao();
    }

    @Provides
    @Singleton
    public CrmDao provideCrmDao(ABSDatabase db) {
        return db.crmDao();
    }
}
