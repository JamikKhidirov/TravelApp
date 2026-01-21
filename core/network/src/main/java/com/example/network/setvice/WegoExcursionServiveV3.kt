package com.example.network.setvice



import com.example.network.wegodata.attractiondata.AttractionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query




interface WegoExcursionServiveV3 {

    @GET("attractions/")
    //Функция будет возаращать список достопримечательностей
    suspend fun getListattraction(
        //Для плагинации
        @Query("page") page: Int = 1,
        @Query("lang") lang: String= "ru",
        //Уникальный айди страны к кому хотим сделать поиск
        @Query("country") country: Int = 3017382,
        //Уникальный идентификатор города
        //id из GET /search/ или GET /cities/
        @Query("city") city: Int = 3,
        @Query("popular") popular: Boolean = true
    ): Response<AttractionResponse>
}