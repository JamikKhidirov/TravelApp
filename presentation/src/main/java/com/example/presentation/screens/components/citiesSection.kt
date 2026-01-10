package com.example.presentation.screens.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.states.actions.HomeAction
import com.example.presentation.states.screen.HomeUiState
import com.example.presentation.uicomponents.buttons.MainButton
import com.example.presentation.uicomponents.dowloads.items.RowItemsShimmerPlaceHolder
import com.example.presentation.uicomponents.vidjets.RowCities




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

