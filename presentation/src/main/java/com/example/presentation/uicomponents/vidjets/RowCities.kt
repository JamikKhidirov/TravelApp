package com.example.presentation.uicomponents.vidjets

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.domain.wegodata.citiesdata.City


@Suppress("PreviewAnnotationInFunctionWithParameters")
@Composable
@Preview(showBackground = true)
fun RowCities(
    modifier: Modifier = Modifier,
    results: List<City>
){

    val state = rememberLazyListState()

    LazyRow(
        modifier = modifier,
        state = state
    ) {
        items(results){ city ->

        }
    }
}

