package com.example.cache.datacache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.cache.datacache.dao.CityDao
import com.example.cache.datacache.data.CityEntity


@Database(
    entities = [CityEntity::class],
    version = 1
)
abstract class CityDatabase: RoomDatabase() {
    abstract fun citydao(): CityDao
}