package com.example.uikit.uicomponents.search

import android.R
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.builtins.MapSerializer


@Composable
fun SearchCard(
    modifier: Modifier = Modifier,
    onClickSeacrCard: () -> Unit
){

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp,
            pressedElevation = 2.dp,
        ),
        onClick = onClickSeacrCard,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
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

            Text(
                text = "Город, экскурсия, билет, достопримечательности",
                modifier = Modifier
                    .padding(end = 10.dp)
                ,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Color.Gray,
                fontSize = 19.sp
            )
        }
    }
}