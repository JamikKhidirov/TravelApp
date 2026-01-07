package com.example.presentation.uicomponents.vidjets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.wegodata.citiesdata.City
import com.example.presentation.uicomponents.vidjets.ImageVidjetDesc.ImagevidjetGetCities



@Composable
fun RowCities(
    modifier: Modifier = Modifier,
    results: List<City>,
    onClickCity: (City) -> Unit
){

    val state = rememberLazyListState()

    LazyRow(
        modifier = modifier,
        state = state,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(results){ city ->
            ImagevidjetGetCities(
                modifier = Modifier,
                city = city,
                onClickCity = onClickCity
            )
        }
    }
}

