package com.example.cache.datacache.di

import android.content.Context
import androidx.activity.contextaware.ContextAware
import androidx.room.Room
import com.example.cache.datacache.dao.CityDao
import com.example.cache.datacache.database.CityDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {


    @Provides
    @Singleton
    fun provideDataBase(
        @ApplicationContext context: Context
    ): CityDatabase {
        return Room.databaseBuilder(
            context,
            CityDatabase::class.java,
            "cities"
        ).build()
    }


    @Provides
    fun provideCityDao(db: CityDatabase): CityDao{
        return db.citydao()
    }
}