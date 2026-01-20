package com.example.presentation.screens.SearchScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigationevent.NavigationEventHistory
import com.example.presentation.destination.ScreenDestination
import com.example.presentation.screens.SearchScreen.uicomponents.SearchBar
import com.example.presentation.screens.SearchScreen.uicomponents.SearchBarVidjet


@Composable
@Preview(showBackground = true)
fun SearchScreen(
    navHostController: NavHostController = rememberNavController()
){


    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { paddingValues ->
        SearchBottomScreen(
            paddingValues = paddingValues,
            history = listOf(),
            navHostController = navHostController,
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
    navHostController: NavHostController,
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
        navHostController = navHostController
    )
}
}
