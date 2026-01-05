package com.example.presentation.uicomponents.tabrow

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.uicomponents.vidjets.TabRowItem


@Composable
@Preview(showBackground = true)
fun TabRowMainScreen(
    modifier: Modifier = Modifier,
    onClickTab: (tab: String) -> Unit = {}
){

    val list: List<String> = listOf(
        "Города",
        "Страны",
        "Категории",
        "Экскурсии"
    )
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        items(list){item ->
            TabRowItem(
                title = item,
                modifier = Modifier,
                onClickCard = onClickTab
            )
        }
    }
}


