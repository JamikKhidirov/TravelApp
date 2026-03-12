package com.example.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController


@Composable
@Preview(showBackground = true)
fun SearchScreen(
    navHostController: NavHostController = rememberNavController()
){

    var searchQuery by remember { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current


    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { paddingValues ->

        LaunchedEffect(Unit) {
            //Открываем клавиатуру при входе на экран
            keyboardController?.show()
        }

        SearchBottomScreen(
            paddingValues = paddingValues,
            history = listOf(),
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            navHostController = navHostController,
            onHistory = { }
        )
    }
}


@Composable
fun SearchBottomScreen(
    paddingValues: PaddingValues,
    history: List<String>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    navHostController: NavHostController,
    popularList: List<String>? = null,
    onHistory: (String) -> Unit

){ Box(
    modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(MaterialTheme.colorScheme.background)
) {
    // 1. СПИСОК (Кладем первым, чтобы он был "под" поиском)
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        // ВАЖНО: делаем отступ сверху, чтобы первый элемент не спрятался под поиском
        contentPadding = PaddingValues(top = 70.dp)
    ) {
        item {
            Text(
                text = "Популярные места",
                modifier = Modifier.padding(
                    start = 15.dp,
                    top = 5.dp
                ),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (popularList?.isNotEmpty() == true){

            // ... ваши items для популярных мест и истории

        }


        if(history.isNotEmpty()){
            item {
                Text(
                    text = "Вы искали",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(
                            start = 15.dp,
                            top = 20.dp
                        )
                )
            }

            //тут список историй
        }

    }

    SearchBarVidjet(
        navHostController = navHostController,
        searchQuery = searchQuery,
        onSearchQueryChange = onSearchQueryChange
    )
}
}
