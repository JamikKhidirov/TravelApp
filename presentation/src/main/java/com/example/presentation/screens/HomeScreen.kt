package com.example.presentation.screens

import android.app.Activity
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_WATCH
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.model.DisplayableItem
import com.example.domain.wegodata.attractiondata.Attraction
import com.example.domain.wegodata.citiesdata.City
import com.example.presentation.uicomponents.search.SearchCard
import com.example.presentation.uicomponents.vidjets.RowCities
import com.example.presentation.uicomponents.vidjets.TabRefresh
import viewmodals.HomeViewModel


@Composable
@Preview(showBackground = true)
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
){

    val cities = viewModel.cities.collectAsStateWithLifecycle()

    val attraction = viewModel.attractionList.collectAsStateWithLifecycle()


    val state = rememberLazyListState()


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
            listAttraction = attraction.value
        )
    }

}



@Composable
fun BottomHomeScreen(
    paddingValues: PaddingValues,
    state: LazyListState,
    listCity: List<City>,
    listAttraction: List<Attraction>,
    onClickCities: (DisplayableItem) -> Unit,
    onRefResh: (Boolean) -> Unit
){

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
                    onClickCity = onClickCities
                )
                if (listCity.isNotEmpty()){
                    //Кнопка показать все
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
                        modifier = Modifier.padding(top = 20.dp, start = 15.dp)
                    )
                }

                RowCities<Attraction>(
                    modifier = Modifier.padding(top = 10.dp),
                    results = listAttraction,
                    onClickCity = {

                    }
                )

                if (listAttraction.isNotEmpty()){

                    //Кнопка показать все

                }

            }

        }


        //Список все экскурсии и билеты
        //items

    }
}
