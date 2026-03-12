package com.example.cache.datacache.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.network.sputnikdata.citydata.CityDto


@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey
    val id : Int,
    val name: String,
    val region_id: Int,
    val country_id: Int
)

fun CityEntity.toCityDto(): CityDto {
    return CityDto(
        id = this.id,
        name = this.name,
        region_id = this.region_id,
        country_id = this.country_id
    )
}

fun CityDto.toCityEntity(): CityEntity{
    return CityEntity(
        id = this.id,
        name = this.name,
        region_id = this.region_id,
        country_id = this.country_id
    )
}