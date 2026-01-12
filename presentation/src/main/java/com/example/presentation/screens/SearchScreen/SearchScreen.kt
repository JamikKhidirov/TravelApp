package com.example.presentation.screens.SearchScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigationevent.NavigationEventHistory
import com.example.presentation.screens.SearchScreen.uicomponents.SearchBar


@Composable
@Preview(showBackground = true)
fun SearchScreen(){


    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
        ,
        topBar = {
            SearchBar(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 15.dp),
                onTextValue = {

                }
            )
        }
    ) { paddingValues ->
        SearchBottomScreen(
            paddingValues = paddingValues,
            history = listOf(),
            onHistory = {

            }
        )
    }
}


@Composable
fun SearchBottomScreen(
    paddingValues: PaddingValues,
    history: List<String>,
    popularList: List<String>? = null,
    onHistory: (String) -> Unit

){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(paddingValues)
    ) {

        item {
            Text(
                text = "Популярные места",
                modifier = Modifier.padding(
                    horizontal = 15.dp,
                    vertical = 10.dp
                ),
                fontSize = 22.sp,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )

        }

        if (popularList?.isNotEmpty() == true){
            item(popularList){
                //Виджет истории поиска
            }
        }




    }
}
