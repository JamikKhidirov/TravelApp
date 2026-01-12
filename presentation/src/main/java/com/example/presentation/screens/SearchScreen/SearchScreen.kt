package com.example.presentation.screens.SearchScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview


@Composable
@Preview(showBackground = true)
fun SearchScreen(){


    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        SearchBottomScreen(
            paddingValues = paddingValues
        )
    }
}


@Composable
fun SearchBottomScreen(
    paddingValues: PaddingValues
){

}