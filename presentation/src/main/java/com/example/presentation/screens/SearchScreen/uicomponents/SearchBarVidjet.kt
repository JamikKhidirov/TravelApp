package com.example.presentation.screens.SearchScreen.uicomponents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@Composable
fun SearchBarVidjet(
    navHostController: NavHostController
){
    Surface(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.background, // Чтобы список не просвечивал сквозь поиск
        shadowElevation = 2.dp // Добавим тень, чтобы отделить от списка при скролле
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(), // Учитываем системную панель
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
                    navHostController.popBackStack()
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
