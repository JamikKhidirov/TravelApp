package com.example.network.setvice

import retrofit2.http.GET
import retrofit2.http.Query


interface WegoExcursionService {

    @GET("/cities/")
    suspend fun getListCities(
        //Для плагинации
        @Query("page") page: Int,
        //Уникальный айди страны к кому хотим сделать поиск
        //id из GET /search/ или GET /countries/
        @Query("country") country: Int,
        //Если trye то сортировка по популярности
        @Query("popular") popular: Boolean
    )



    @GET(" /attractions/")
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

    @GET(" /search/")
    suspend fun searchList(
        // Строковый поисковый запрос Любые строковые
        @Query("query") query: String,
        //	Код валюты для показа цен у товаров
        //code из GET /currencies/
        @Query("currency") code: String = "RUB"

    )




}