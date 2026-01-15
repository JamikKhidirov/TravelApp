package com.example.presentation.states.actions

import com.example.domain.wegodata.attractiondata.Attraction
import com.example.domain.wegodata.citiesdata.City
import com.example.domain.wegodata.productpopular.Tour


sealed interface HomeAction{
    data class ChangeTab(val isPopular: Boolean) : HomeAction
    object LoadMoreCities : HomeAction
    object LoadMoreAttractions : HomeAction
    object LoadMoreTours : HomeAction
    data class OnCityClick(val city: City) : HomeAction
    data class OnAttractionClick(val value: Attraction): HomeAction
    data class OnTourClick(val value: Tour): HomeAction

    object SeeAllAttractions: HomeAction


    object Retry : HomeAction

}