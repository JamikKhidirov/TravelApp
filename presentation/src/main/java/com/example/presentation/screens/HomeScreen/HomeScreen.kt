package com.example.presentation.screens.HomeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.screens.OnBottomReached
import com.example.presentation.screens.HomeScreen.uicomponents.attractionsSection
import com.example.presentation.screens.HomeScreen.uicomponents.citiesSection
import com.example.presentation.screens.HomeScreen.uicomponents.toursSection
import com.example.presentation.states.actions.HomeAction
import com.example.presentation.states.screen.HomeUiState
import com.example.presentation.uicomponents.search.SearchCard
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

        // Секция Городов
        citiesSection(uiState, onAction)

        //Секция Достопримечательностей
        attractionsSection(uiState, onAction)

        //Вертикальный список Туров
        toursSection(uiState, onAction)
    }
}

