package com.example.uikit.statescreen.LoadingScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uikit.uicomponents.dowloads.ShimmerPlaceholder
import com.example.uikit.uicomponents.dowloads.items.PopularTourItemShimmer


@Composable
@Preview(showBackground = true)
fun HomeSkeletonScreen(
    paddingValues: PaddingValues = PaddingValues()
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(top = 24.dp) // то же, что и у SearchCard
    ) {

        /* 1. SearchCard – точные размеры и отступы */
        stickyHeader {
            ShimmerPlaceholder(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }

        /* 2. TabRefresh – 2 текста "Ближащие | Популярные" */
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                repeat(2) {
                    ShimmerPlaceholder(
                        modifier = Modifier
                            .width(110.dp)
                            .height(24.dp),
                        shape = RoundedCornerShape(15.dp)
                    )
                }
            }
        }

        // citiesSection – горизонтальный RowCities
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(4) {
                    ShimmerPlaceholder(
                        modifier = Modifier
                            .width(200.dp)
                            .height(260.dp),
                        shape = RoundedCornerShape(15.dp)
                    )
                }
            }
        }

        /* 4. Кнопка "Показать все" (если города есть) – делаем такую же */
        item {
            ShimmerPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .height(48.dp),          // высота MainButton
                shape = RoundedCornerShape(12.dp)
            )
        }

        /* 5. attractionsSection – заголовок + RowCities */
        item {
            Column {
                /* заголовок "Еще популярные места" */
                ShimmerPlaceholder(
                    modifier = Modifier
                        .padding(top = 25.dp, start = 15.dp)
                        .width(220.dp)
                        .height(24.dp),      // 20.sp
                    shape = RoundedCornerShape(4.dp)
                )

                /* горизонтальный список */
                LazyRow(
                    modifier = Modifier.padding(top = 10.dp),
                    contentPadding = PaddingValues(horizontal = 15.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(3) {
                        ShimmerPlaceholder(
                            modifier = Modifier
                                .width(200.dp)
                                .height(260.dp),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
        }

        /* 6. Кнопка "Показать все" attractions */
        item {
            ShimmerPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp)
            )
        }

        /* 7. toursSection – заголовок + PopularTourItemShimmer */
        item {
            Column {
                /* заголовок "Популярные туры" */
                ShimmerPlaceholder(
                    modifier = Modifier
                        .padding(top = 25.dp, start = 15.dp, bottom = 8.dp)
                        .width(160.dp)
                        .height(24.dp),
                    shape = RoundedCornerShape(4.dp)
                )

                /* ваш готовый шиммер-список туров */
                PopularTourItemShimmer()
            }
        }
    }
}