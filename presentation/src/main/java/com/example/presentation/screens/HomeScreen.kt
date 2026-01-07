package com.example.presentation.screens

import android.app.Activity
import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.content.res.Configuration.UI_MODE_TYPE_WATCH
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.example.presentation.uicomponents.search.SearchCard
import com.example.presentation.uicomponents.vidjets.TabRefresh


@Composable
@Preview(showBackground = true)
fun HomeScreen(){

    val state = rememberLazyListState()


    Scaffold(
        modifier = Modifier
            .fillMaxSize(),

        bottomBar = {

        }
    ) { paddingValues ->

        BottomHomeScreen(
            paddingValues = paddingValues,
            state = state
        )
    }

}



@Composable
fun BottomHomeScreen(
    paddingValues: PaddingValues,
    state: LazyListState
){

    LazyColumn(
        state = state,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

        stickyHeader {
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

                }
            )
        }


        //В ров список ближащих или популярных мест
        item {

        }

        //Популярные места
        //В ров тоже список
        item {

        }


        //Список все экскурсии и билеты

    }
}
