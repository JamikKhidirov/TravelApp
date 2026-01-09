package com.example.presentation.uicomponents.dowloads.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.uicomponents.dowloads.ShimmerPlaceholder


@Composable
fun RowItemsShimmerPlaceHolder(
    modifier: Modifier = Modifier.padding(top = 10.dp)
){
    Column(
        modifier = modifier
    ) {
        Row( modifier = Modifier.padding(start = 15.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            repeat(5) { // Показываем 5 фейковых карточек в ряд
                ShimmerPlaceholder(
                    modifier = Modifier
                        .width(200.dp) // ОБЯЗАТЕЛЬНО: укажите ширину для LazyRow
                        .height(260.dp),
                    shape = RoundedCornerShape(15.dp)
                )
            }
        }
        ShimmerPlaceholder(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(top = 10.dp)
                .padding(horizontal = 15.dp),
            shape = RoundedCornerShape(15.dp)
        )
    }
}