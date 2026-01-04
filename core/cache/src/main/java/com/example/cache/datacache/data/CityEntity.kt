package com.example.cache.datacache.data

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey
    val id : Int,
    val name: String,
    val region_id: Int,
    val country_id: Int
)
