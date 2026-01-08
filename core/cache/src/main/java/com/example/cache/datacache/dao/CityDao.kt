package com.example.cache.datacache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cache.datacache.data.CityEntity


@Dao
interface CityDao {

    @Query("SELECT * FROM cities")
    suspend fun getAll(): List<CityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cities: List<CityEntity>)


    @Query("DELETE FROM cities")
    suspend fun clear()
}