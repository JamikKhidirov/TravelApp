package com.example.home.uicomponents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.home.action.HomeAction
import com.example.home.state.HomeUiState
import com.example.uikit.uicomponents.buttons.MainButton
import com.example.uikit.uicomponents.dowloads.items.RowItemsShimmerPlaceHolder
import com.example.uikit.uicomponents.vidjets.RowCities


fun LazyListScope.citiesSection(state: HomeUiState, onAction: (HomeAction) -> Unit) {
    item {
        val citiesState = state.citiesState

        if (citiesState.items.isEmpty() && citiesState.isLoading) {
            RowItemsShimmerPlaceHolder()
        } else {
            Column {
                RowCities(
                    results = citiesState.items,
                    onClickCity = { onAction(HomeAction.OnCityClick(it)) },
                    isLoading = citiesState.isLoading,
                    onLoadMore = { onAction(HomeAction.LoadMoreCities) }
                )
                if (citiesState.items.isNotEmpty()) {
                    MainButton(
                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                        onClickButton = { /* действие */ }
                    )
                }
            }
        }
    }
}

