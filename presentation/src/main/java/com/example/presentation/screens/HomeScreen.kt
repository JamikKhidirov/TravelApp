package com.example.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.uicomponents.search.SearchCard


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
                    .padding(top = 10.dp)
            )
        }

        //Таб и список ближащих или популярныъ мест
        //В ров список
        item {

        }

        //Популярные места
        //В ров тоже список
        item {

        }


        //Список все экскурсии и билеты

    }
}
