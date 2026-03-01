package com.example.uikit.uicomponents.bars

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.uikit.uicomponents.bars.data.BottomBarData
import kotlin.collections.listOf


@Composable
@Preview(showBackground = true)
fun BottomBarCastom(
    modifier: Modifier = Modifier,
    currentTab: Int = 0,
    onTabSelected: (Int) -> Unit = {},
){

    val currenttab = remember(currentTab) { mutableStateOf(currentTab) }

    val tabs = remember {
        listOf(
            BottomBarData(title = "Дом", icon = Icons.Default.Home),
            BottomBarData(title = "Поиск", icon = Icons.Default.Search),
            BottomBarData(title = "Избранные", icon = Icons.Default.Favorite),
            BottomBarData(title = "Профиль", icon = Icons.Default.Person),
        )
    }

    val glassColor = MaterialTheme
        .colorScheme.surface.copy(alpha = 0.63f)

    Row(
        modifier
            .fillMaxWidth()
            // Тень ДО clip и background для внешнего эффекта
            .shadow(
                elevation = 8.dp,  // Увеличьте для заметности
                shape = RoundedCornerShape(20.dp),
                spotColor = Color.Gray.copy(0.9f),
                ambientColor = Color.Gray.copy(0.9f)
            )
            // Теперь clip после тени
            .clip(RoundedCornerShape(20.dp))
            .background(color = glassColor)
            // Border после background, если нужно
            .border(
                width = 1.dp,
                color = Color.Gray.copy(0.5f),
                shape = RoundedCornerShape(20.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        tabs.forEachIndexed { index, data ->
            IconButton(
                colors =  IconButtonDefaults.iconButtonColors(
                    containerColor = if (currentTab == index) Color.Blue.copy(0.5f)
                    else Color.Gray.copy(0.3f),
                ),
                onClick = {
                    onTabSelected(index)
                },
                modifier = Modifier
                    .size(55.dp)
                    .padding(
                        vertical = 7.dp
                    )
            ) {
                Icon(
                    data.icon,
                    contentDescription = "Иконка таба",
                    tint = if (currentTab == index) Color.White.copy(0.8f)
                    else Color.Gray.copy(0.7f),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}