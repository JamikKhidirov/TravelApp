package com.example.presentation.uicomponents.vidjets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.domain.model.DisplayableItem
import com.example.domain.wegodata.citiesdata.City
import com.example.presentation.uicomponents.vidjets.ImageVidjetDesc.ImagevidjetGetCities



@Composable
fun RowCities(
    modifier: Modifier = Modifier,
    results: List<DisplayableItem>,
    onClickCity: (DisplayableItem) -> Unit
){

    val state = rememberLazyListState()

    LazyRow(
        modifier = modifier
            .animateContentSize(),
        state = state,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(results, key = {
            it.id
        }){ city ->
            ImagevidjetGetCities(
                modifier = Modifier.animateItem(), // 🔥 ВОТ ОНО,
                city = city,
                onClickCity = onClickCity
            )
        }
    }
}

