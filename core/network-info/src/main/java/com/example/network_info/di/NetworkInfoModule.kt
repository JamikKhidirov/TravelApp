package com.example.network_info.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkInfoModule {

    @Singleton
    @Provides
    fun providerConnectivityManager(
        @ApplicationContext context: Context
    ): ConnectivityManager {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return manager
    }

    @Provides
    @Singleton
    fun providerTelephoneManager(
        @ApplicationContext context: Context
    ): TelephonyManager {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager
    }

    @Provides
    @Singleton
    fun providerWifiManager(
        @ApplicationContext context: Context
    ): WifiManager {
        val manager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return manager
    }
}