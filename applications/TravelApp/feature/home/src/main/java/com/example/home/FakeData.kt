package com.example.home

import com.example.home.action.HomeAction
import com.example.home.viewmodel.HomeViewModel
import com.example.network.wegodata.attractiondata.Attraction
import com.example.network.wegodata.citiesdata.City
import com.example.network.wegodata.productpopular.Tour
import com.example.network.wegodata.productpopular.TourAuthor
import com.example.network.wegodata.productpopular.TourCity
import com.example.network.wegodata.productpopular.TourTags
import kotlinx.coroutines.flow.MutableStateFlow


object FakeData {
    val fakeCities = listOf(
        City(
            id = 1,
            name = "Махачкала",
            slug = "makhachkala",
            preview = "https://example.com/makhachkala.jpg",
            itemsCount = 12,
            country = "Россия"
        ),
        City(
            id = 2,
            name = "Санкт‑Петербург",
            slug = "spb",
            preview = "https://example.com/spb.jpg",
            itemsCount = 25,
            country = "Россия"
        )
    )

    val fakeAttractions = listOf(
        Attraction(
            id = 1,
            name = "Старый город",
            slug = "old-town",
            preview = "https://example.com/old-town.jpg",
            itemsCount = 5
        ),
        Attraction(
            id = 2,
            name = "Морская набережная",
            slug = "seaside-promenade",
            preview = "https://example.com/promenade.jpg",
            itemsCount = 3
        )
    )

    val fakeTours = listOf(
        Tour(
            id = 1,
            title = "Экскурсия по старому городу",
            slug = "old-city-tour",
            cover = "https://example.com/old-city-tour.jpg",
            preview = "https://example.com/old-city-tour-preview.jpg",
            price = 1200.0,
            exprice = 1500.0,
            currency = "₽",
            currencyCode = "RUB",
            rating = 4.8,
            reviewsCount = 127,
            ratingsCount = 135,
            category = "Туры",
            city = TourCity(
                id = 1,
                name = "Махачкала",
                slug = "Махачкала"
            ),
            duration = "2 часа",
            durationMin = 120,
            durationMax = 120,
            type = 1,
            tags = TourTags(/* подставь нужные поля */),
            locale = "ru",
            author = TourAuthor(/* заполни по необходимости */)
        )
    )
}

