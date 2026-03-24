package com.example.notification

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
@Preview(showBackground = true)
fun NotifictionScreen(){

    val listState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerpadding ->
        BottomNotificationScreen(
            modifier = Modifier.padding(innerpadding),
            listState = listState
        )
    }

}


@Composable
fun BottomNotificationScreen(
    modifier: Modifier = Modifier,
    listState: LazyListState
){
    LazyColumn(
        modifier = modifier,
        state = listState
    ) {

    }
}