package com.example.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.search.uicomponents.SearchBar


@Composable
fun SearchBarVidjet(
    navHostController: NavHostController,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
){
    Surface(
        modifier = Modifier,
        color = MaterialTheme.colorScheme.background, // Чтобы список не просвечивал сквозь поиск
        shadowElevation = 2.dp // Добавим тень, чтобы отделить от списка при скролле
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(), // Учитываем системную панель
            verticalAlignment = Alignment.CenterVertically,
        ) {

            // ← ИСПРАВЛЕНО: Icon + clickable вместо IconButton
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Кнопка назад",
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp)
                    .clip(RoundedCornerShape(30.dp))// Фиксированный размер
                    .clickable { navHostController.popBackStack() },
                tint = Color.Black

            )


            SearchBar(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 15.dp),
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewSearchVidject(
    modifier: Modifier = Modifier
){
    SearchBarVidjet(
        navHostController = rememberNavController(),
        searchQuery = "",
        onSearchQueryChange = {

        }
    )
}
