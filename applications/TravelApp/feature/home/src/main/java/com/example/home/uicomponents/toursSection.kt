package com.example.home.uicomponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.home.action.HomeAction
import com.example.home.state.HomeUiState
import com.example.uikit.uicomponents.dowloads.items.PopularTourItemShimmer
import com.example.home.uikit.PopularTourItem


fun LazyListScope.toursSection(
    uiState: HomeUiState,
    onAction: (HomeAction) -> Unit
) {
    val toursState = uiState.popularToursState

    // 1. Заголовок
    if (toursState.items.isNotEmpty()) {
        item(key = "tours_header") { // Добавьте key для статического item
            Text(
                text = "Популярные туры",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 25.dp, start = 15.dp, bottom = 8.dp)
            )
        }
    }

    // 2. Первоначальная загрузка (Шиммер)
    if (toursState.items.isEmpty() && toursState.isLoading) {
        item(key = "tours_shimmer") {
            PopularTourItemShimmer()
        }
    }

    // 3. Основной список
    items(
        items = toursState.items,
        key = { it.id }
    ) { tour ->
        // Запоминаем клик, чтобы не создавать новую функцию при каждом вызове
        val onClick = remember(tour.id, onAction) {
            { onAction(HomeAction.OnTourClick(tour)) }
        }

        PopularTourItem(
            tour = tour,
            onClick = onClick
        )
    }

    // 4. Индикатор дозагрузки (Footer)
    if (toursState.isLoading && toursState.items.isNotEmpty()) {
        item(key = "tours_loader") {
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