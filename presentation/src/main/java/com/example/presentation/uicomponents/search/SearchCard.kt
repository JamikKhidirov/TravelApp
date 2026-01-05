package com.example.presentation.uicomponents.search

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.serialization.builtins.MapSerializer


@Composable
@Preview(showBackground = true)
fun SearchCard(
    modifier: Modifier = Modifier,
    onClickSeacrCard: () -> Unit = {}
){

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 0.dp,
        ),
        onClick = onClickSeacrCard,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
              ,
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Иконка для поиска",
                modifier = Modifier
                    .padding(10.dp)
                    .size(24.dp),
                tint = Color.Blue,
            )
        }
    }
}