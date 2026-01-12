package com.example.presentation.screens.SearchScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.presentation.screens.SearchScreen.uicomponents.SearchBar


@Composable
@Preview(showBackground = true)
fun SearchScreen(){


    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { paddingValues ->
        SearchBottomScreen(
            paddingValues = paddingValues,
            onBarSeacrch = { searchText ->

            }
        )
    }
}


@Composable
fun SearchBottomScreen(
    paddingValues: PaddingValues,
    onBarSeacrch: (String) -> Unit
){
    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(paddingValues)
    ) {

        item {
            SearchBar(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 15.dp),
                onTextValue = onBarSeacrch
            )
        }


    }

}