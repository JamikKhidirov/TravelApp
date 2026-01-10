package com.example.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScrollModifierNode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.wegodata.attractiondata.Attraction
import com.example.domain.wegodata.citiesdata.City
import com.example.domain.wegodata.productpopular.Tour
import com.example.presentation.modifiers.shimerEffect
import com.example.presentation.screens.components.attractionsSection
import com.example.presentation.screens.components.citiesSection
import com.example.presentation.screens.components.toursSection
import com.example.presentation.states.actions.HomeAction
import com.example.presentation.states.screen.HomeUiState
import com.example.presentation.uicomponents.buttons.MainButton
import com.example.presentation.uicomponents.dowloads.ShimmerPlaceholder
import com.example.presentation.uicomponents.dowloads.items.PopularTourItemShimmer
import com.example.presentation.uicomponents.dowloads.items.RowItemsShimmerPlaceHolder
import com.example.presentation.uicomponents.search.SearchCard
import com.example.presentation.uicomponents.vidjets.PopularTourItem
import com.example.presentation.uicomponents.vidjets.RowCities
import com.example.presentation.uicomponents.vidjets.TabRefresh
import viewmodals.HomeViewModel


@Composable
@Preview(showBackground = true)
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()



    val state = rememberLazyListState()


    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {paddingValues ->
        BottomHomeScreen(
            paddingValues = paddingValues,
            uiState = uiState,
            onAction = viewModel::handleAction
        )
    }
}



@Composable
fun BottomHomeScreen(
    uiState: HomeUiState,
    paddingValues: PaddingValues,
    onAction: (HomeAction) -> Unit
) {
    val state = rememberLazyListState()

    // Слушаем конец списка для туров
    OnBottomReached(state = state) {
        onAction(HomeAction.LoadMoreTours)
    }

    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize(),
        contentPadding = paddingValues
    ) {
        // 1. Поиск
        stickyHeader {
            SearchCard(
                modifier = Modifier
                    .statusBarsPadding()
                    ,
                onClickSeacrCard = {

                }
            )
        }

        // 2. Табы
        item {
            TabRefresh(
                onItemSelected = { tab ->
                    onAction(HomeAction.ChangeTab(isPopular = tab == "Популярные"))
                }
            )
        }

        // 3. Секция Городов
        citiesSection(uiState, onAction)

        // 4. Секция Достопримечательностей
        attractionsSection(uiState, onAction)

        // 5. Вертикальный список Туров
        toursSection(uiState, onAction)
    }
}

