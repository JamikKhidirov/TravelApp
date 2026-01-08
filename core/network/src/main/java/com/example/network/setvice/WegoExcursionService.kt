package com.example.network.setvice


import com.example.domain.wegodata.attractiondata.AttractionResponse
import com.example.domain.wegodata.citiesdata.CityResponse
import com.example.domain.wegodata.contrydata.CountryResponse
import com.example.domain.wegodata.datareviews.ReviewsResponse
import com.example.domain.wegodata.productdatailinfodata.TourDetailResponse
import com.example.domain.wegodata.productpopular.TourResponse
import com.example.domain.wegodata.searchdata.CityDetailResponse
import com.example.network.state.WeGoApi
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query




interface WegoExcursionService {


    @GET("countries")
    //Получения списка стран
    suspend fun getCountries(
        //Плагинатор сколько стран мы хотим показать
        @Query("page") page: Int
    ): Response<CountryResponse>


    @GET("cities/")
    suspend fun getListCities(
        //Для плагинации
        @Query("page") page: Int = 1,
        @Query("lang") lang: String= "ru",
        //Уникальный айди страны к кому хотим сделать поиск
        //id из GET /search/ или GET /countries/
        @Query("country") country: Int = 0,
        //Если trye то сортировка по популярности
        @Query("popular") popular: Boolean = false
    ): Response<CityResponse>





    @GET("search")
    suspend fun searchList(
        // Строковый поисковый запрос Любые строковые
        @Query("query") query: String,
        //	Код валюты для показа цен у товаров
        //code из GET /currencies/
        @Query("currency") code: String = "RUB"
    ): Response<CityDetailResponse>


    @GET("products/popular/")
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
        @Query("city") city: Int? = null,
        //Уникальный индентификатор достопримечательности
        //id из GET /search/ или GET /attractions/
        @Query("attraction") attraction: Int,
        //Тип сортировки
        //popularity (по умолчанию) или random
        @Query("order") popularity: String
    ): Response<TourResponse>


    @GET("products/{ID}")
    suspend fun getInfoProducts(
        @Path("ID") id: Int,
        @Query("currency") currency: String = "RUB"
    ): Response<TourDetailResponse>


    //Получение отзывов
    @GET("products/{ID}/reviews")
    suspend fun getReviews(
        @Path("ID") id: Int,
        @Query("page") page: Int
    ): Response<ReviewsResponse>



}


