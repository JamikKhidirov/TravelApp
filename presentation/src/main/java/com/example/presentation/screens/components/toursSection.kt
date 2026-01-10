package com.example.presentation.screens.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.states.actions.HomeAction
import com.example.presentation.states.screen.HomeUiState
import com.example.presentation.uicomponents.dowloads.items.PopularTourItemShimmer
import com.example.presentation.uicomponents.vidjets.PopularTourItem


fun LazyListScope.toursSection(
    uiState: HomeUiState,
    onAction: (HomeAction) -> Unit
) {
    val toursState = uiState.popularToursState

    // 1. Заголовок
    if (toursState.items.isNotEmpty()) {
        item {
            Text(
                text = "Популярные туры",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 25.dp, start = 15.dp, bottom = 8.dp)
            )
        }
    }

    // 2. Первоначальная загрузка (Шиммер)
    if (toursState.items.isEmpty() && toursState.isLoading) {
        item {
            PopularTourItemShimmer()
        }
    }

    // 3. Основной список
    // Используем уникальный ID для оптимизации Compose
    items(
        items = toursState.items,
        key = { it.id }
    ) { tour ->
        PopularTourItem(
            tour = tour,
            onClick = { onAction(HomeAction.OnTourClick(tour)) }
        )
    }

    // 4. Индикатор дозагрузки в самом низу (Footer)
    if (toursState.isLoading && toursState.items.isNotEmpty()) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(42.dp),
                    strokeWidth = 4.dp,
                    color = Color(0XFFFF8C00)
                )
            }
        }
    }
}