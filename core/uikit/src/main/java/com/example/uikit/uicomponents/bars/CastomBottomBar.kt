package com.example.uikit.uicomponents.bars

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uikit.uicomponents.bars.data.BottomBarData
import kotlin.collections.listOf


@Composable
@Preview(showBackground = true)
fun CastomBottomBar(
    modifier: Modifier = Modifier,
    currentTab: Int = 0,
    onTabSelected: (Int) -> Unit = {},
) {
    val tabs = remember {
        listOf(
            BottomBarData(title = "Дом", icon = Icons.Default.Home),
            BottomBarData(title = "Поиск", icon = Icons.Default.Search),
            BottomBarData(title = "Избранные", icon = Icons.Default.Favorite),
            BottomBarData(title = "Профиль", icon = Icons.Default.Person),
        )
    }


    val glassColor = MaterialTheme
        .colorScheme.surface.copy(alpha = 0.65f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                topStart = 15.dp,
                topEnd = 15.dp
                )
            )
            .background(color = glassColor)
            .border(
                width = 1.dp,
                color = Color.Gray.copy(0.3f),
                shape =  RoundedCornerShape(
                    topStart = 15.dp,
                    topEnd = 15.dp
                )
            )
    ){
        NavigationBar(
            modifier = modifier,
            containerColor = Color.Transparent,
            tonalElevation = 0.5.dp,
            )
        {

            tabs.forEachIndexed { index, tab ->
                NavigationBarItem(
                    modifier = Modifier,
                    selected = index == currentTab,
                    onClick = { onTabSelected(index) },
                    label = {
                        Text(
                            tab.title,
                            fontSize = 12.sp
                        ) },
                    icon = {
                        Icon(
                            tab.icon,
                            modifier = Modifier.size(36.dp),
                            contentDescription = tab.title
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Blue.copy(0.6f),
                        indicatorColor = Color.Transparent,
                        selectedTextColor = Color.Blue.copy(0.6f),
                        unselectedIconColor = Color.Gray.copy(0.7f),
                        unselectedTextColor = Color.Gray.copy(0.7f)
                    )
                )
            }

        }
    }
}

