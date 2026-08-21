package com.example.bluetooth.di


import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object BluetoothModule {


    @Provides
    @Singleton
    fun providerBluetothAdapter(
        @ApplicationContext ctx: Context
    ): BluetoothAdapter{
        val manager = ctx.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return manager.adapter
    }
}