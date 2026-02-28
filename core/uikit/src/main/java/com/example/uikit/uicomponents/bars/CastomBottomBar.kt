package com.example.uikit.uicomponents.bars

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlin.collections.listOf


@Composable
@Preview(showBackground = true)
fun CastomBottomBar(
    modifier: Modifier = Modifier,
    onClick: (tab: Int) -> Unit = {}
){
    val listTabItem = remember { mutableStateListOf<String>()}

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

    }
}

