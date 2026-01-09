package com.example.presentation.screens

import android.app.Activity
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_WATCH
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.imageLoader
import com.example.domain.model.DisplayableItem
import com.example.domain.wegodata.attractiondata.Attraction
import com.example.domain.wegodata.citiesdata.City
import com.example.domain.wegodata.productpopular.Tour
import com.example.presentation.uicomponents.buttons.MainButton
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

    val context = LocalContext.current
    val imageLoader = context.imageLoader

    val cities = viewModel.cities.collectAsStateWithLifecycle()
    val attraction = viewModel.attractionList.collectAsStateWithLifecycle()
    val popularTours = viewModel.popularTours.collectAsStateWithLifecycle()

    val isNextCitiesLoading = viewModel.isNextCitiesPageLoading.collectAsStateWithLifecycle()
    val isNextPopularLoading = viewModel.isNextPopularPageLoading.collectAsStateWithLifecycle()
    val isNextAttractionLoading = viewModel.isNextAttractionPageLoading.collectAsStateWithLifecycle()


    val state = rememberLazyListState()


    DisposableEffect(Unit) {

        onDispose {
            imageLoader.memoryCache?.clear()
            imageLoader.diskCache?.clear()
        }
    }


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),

        bottomBar = {

        }
    ) { paddingValues ->

        BottomHomeScreen(
            paddingValues = paddingValues,
            state = state,
            listCity = cities.value,
            onClickCities = { city ->
                //что то делаем
            },
            onRefResh = {
                viewModel.setPopular(
                    value = it
                )
            },
            listAttraction = attraction.value,
            listPopular = popularTours.value,
            onClickAttraction = { attraction ->

            },
            onClickPopular = {

            },
            onClickTopBarAllVizBtn = {

            },
            onClickAllVizPopularBtn = {

            },
            isNextPopularLoading = isNextPopularLoading.value ,
            onLoadMorePopular = {
                viewModel.loadPopular()
            },
            isNextCitiesLoading = isNextCitiesLoading.value,
            onLoadMoreCities = {
                viewModel.loadCities(
                    popular = true
                )
            },
            isNextAttractionLoading = isNextAttractionLoading.value,
            onLoadMoreAttraction = {
                viewModel.loadAttreaction()
            }
        )
    }

}



@Composable
fun BottomHomeScreen(
    paddingValues: PaddingValues,
    state: LazyListState,
    listCity: List<City>,
    listAttraction: List<Attraction>,
    listPopular: List<Tour>,
    isNextPopularLoading: Boolean,
    onLoadMorePopular: () -> Unit,
    isNextCitiesLoading: Boolean,
    onLoadMoreCities: () -> Unit,
    isNextAttractionLoading: Boolean,
    onLoadMoreAttraction: () -> Unit,
    onClickCities: (City) -> Unit,
    onClickAttraction: (Attraction) -> Unit,
    onClickPopular: (Tour) -> Unit,
    onRefResh: (Boolean) -> Unit,
    onClickTopBarAllVizBtn: () -> Unit,
    onClickAllVizPopularBtn: ()-> Unit
){

    // Отслеживаем конец основного списка
    val shouldLoadMorePopular = remember {
        derivedStateOf {
            val lastVisibleItem = state.layoutInfo.visibleItemsInfo.lastOrNull() ?: return@derivedStateOf false
            lastVisibleItem.index >= state.layoutInfo.totalItemsCount - 1
        }
    }


    LaunchedEffect(shouldLoadMorePopular.value) {
        if (shouldLoadMorePopular.value && !isNextPopularLoading) {
            onLoadMorePopular()
        }
    }

    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        stickyHeader {
            //Поиск стран и городов куда хочет поехать пользователей
            SearchCard(
                modifier = Modifier
                    .padding(top = 10.dp),
                onClickSeacrCard = {

                }
            )
        }

        //Таб обновления популярных илии ближащих мест
        item {
            TabRefresh(
                modifier = Modifier,
                onItemSelected = { tabItem: String ->
                    when(tabItem){
                        "Ближащие" -> onRefResh(false)
                        "Популярные" -> onRefResh(true)
                    }
                }
            )
        }


        //В ров список ближащих или популярных мест
        item {
            Column {
                RowCities<City>(
                    modifier = Modifier,
                    results = listCity,
                    onClickCity = onClickCities,
                    isLoading = isNextCitiesLoading,
                    onLoadMore = onLoadMoreCities
                )
                if (listCity.isNotEmpty()){
                    //Кнопка показать все
                    MainButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .padding(top = 10.dp),
                        onClickButton = onClickTopBarAllVizBtn
                    )
                }
            }
        }

        //Популярные места
        //В ров тоже список
        item {
            Column {
                if (listAttraction.isNotEmpty()){
                    Text(
                        text = "Еще популярные места",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 25.dp, start = 15.dp)
                    )
                }

                RowCities<Attraction>(
                    modifier = Modifier.padding(top = 10.dp),
                    results = listAttraction,
                    onClickCity = onClickAttraction,
                    isLoading = isNextAttractionLoading,
                    onLoadMore = onLoadMoreAttraction
                )

                if (listAttraction.isNotEmpty()){
                    //Кнопка показать все
                    MainButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .padding(top = 10.dp),
                        onClickButton = onClickAllVizPopularBtn
                    )
                }

            }

        }


        //Список все экскурсии и билеты
        //items
        // 🔽 Вертикальный список популярных туров
        if (listPopular.isNotEmpty()) {
            item {
                Text(
                    text = "Популярные туры",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 25.dp, start = 15.dp, bottom = 8.dp)
                )

            }
        }

        items(
            items = listPopular,
            key = { it.id }
        ) { tour ->
            PopularTourItem(
                tour = tour,
                onClick = { onClickPopular(tour) }
            )
        }

        // Индикатор загрузки в самом низу экрана для туров
        if (isNextPopularLoading) {
            item {
                Box(Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp).padding(8.dp),
                        strokeWidth = 2.dp,
                        color = Color.Blue
                    )
                }
            }
        }
    }
}
