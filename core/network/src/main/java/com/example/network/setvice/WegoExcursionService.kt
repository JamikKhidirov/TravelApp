package com.example.network.setvice

import com.example.network.state.WeGoApi
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query



@WeGoApi
interface WegoExcursionService {

    @GET("cities")
    suspend fun getListCities(
        //Для плагинации
        @Query("page") page: Int,
        //Уникальный айди страны к кому хотим сделать поиск
        //id из GET /search/ или GET /countries/
        @Query("country") country: Int,
        //Если trye то сортировка по популярности
        @Query("popular") popular: Boolean
    )



    @GET("attractions")
    //Функция будет возаращать список достопримечательностей
    suspend fun getListattraction(
        //Для плагинации
        @Query("page") page: Int,
        //Уникальный айди страны к кому хотим сделать поиск
        @Query("country") country: Int,
        //Уникальный идентификатор города
        //id из GET /search/ или GET /cities/
        @Query("city") city: Int

    )

    @GET("search")
    suspend fun searchList(
        // Строковый поисковый запрос Любые строковые
        @Query("query") query: String,
        //	Код валюты для показа цен у товаров
        //code из GET /currencies/
        @Query("currency") code: String = "RUB"
    )


    @GET("products/popular")
    suspend fun getPopularProducts(
        //Для плагинации
        @Query("page") page: Int,
        //Код языка товаров (контент, описание)
        @Query("lang") lang: String = "ru",
        //Код валют для показа цен у товары
        @Query("currency") currency: String = "RUB",
        //Уникальный айди страны к кому хотим сделать поиск
        //	id из GET /search/ или GET /countries/
        @Query("country") country: Int,
        //Уникальный идентификатор города
        //id из GET /search/ или GET /cities/
        @Query("city") city: Int,
        //Уникальный индентификатор достопримечательности
        //id из GET /search/ или GET /attractions/
        @Query("attraction") attraction: String,
        //Тип сортировки
        //popularity (по умолчанию) или random
        @Query("order") popularity: String
    )


    @GET("products/{ID}")
    suspend fun getInfoProducts(
        @Path("ID") id: Int,
        @Query("currency") currency: String = "RUB"
    )


    @GET("products/{ID}/reviews")
    suspend fun getReviews(
        @Path("ID") id: Int,
        @Query("page") page: Int
    )




}