package com.example.battery.di

import android.content.Context
import android.os.BatteryManager
import com.example.battery.data.BatteryDataSourceImpl
import com.example.battery.domain.BatteryDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(ActivityRetainedComponent::class)
@Module
object BatteryModule {


    @Provides
    @ActivityRetainedScoped
    fun provideBatteryManager(
        @ApplicationContext context: Context
    ): BatteryManager {
        return context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
    }

    @Provides
    @ActivityRetainedScoped
    fun provideBatteryDataSource(
        batteryManager: BatteryManager
    ): BatteryDataSource {
        return BatteryDataSourceImpl(batteryManager)
    }
}