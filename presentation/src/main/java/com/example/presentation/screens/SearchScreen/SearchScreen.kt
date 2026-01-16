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
        contentPadding = PaddingValues(top = 80.dp)
    ) {
        item {
            Text(
                text = "Популярные места",
                modifier = Modifier.padding(
                    start = 15.dp,
                    top = 10.dp
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
                            top = 20.dp)
                )
            }

            //тут список историй
        }

    }


    Surface(
        modifier = Modifier.align(Alignment.TopCenter),
        color = MaterialTheme.colorScheme.background, // Чтобы список не просвечивал сквозь поиск
        shadowElevation = 4.dp // Добавим тень, чтобы отделить от списка при скролле
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding() // Учитываем системную панель
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 15.dp),
                onTextValue = {

                }
            )

            TextButton(
                modifier = Modifier
                    .padding(end = 15.dp),
                onClick = {
                    //Обработка кнопки отмены
                }
            ) {
                Text(
                    text = "Отмена",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
}
