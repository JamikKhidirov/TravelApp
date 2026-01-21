package com.example.uikit.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun SwipeRefreshScreen(
    isRefresh: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier,
    conteinerColor: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit
){
    val state = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = isRefresh,
        onRefresh = onRefresh,
        state = state,
        modifier = modifier.fillMaxSize()
            .background(conteinerColor)
    ){
        content()
    }

}