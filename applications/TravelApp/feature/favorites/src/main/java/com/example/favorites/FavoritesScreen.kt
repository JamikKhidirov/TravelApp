package com.example.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.favorites.action.FavoritesAction
import com.example.favorites.state.FavoritesUiState
import com.example.favorites.viewmodel.FavoritesViewModel
import com.example.home.uikit.PopularTourItem
import com.example.uikit.uicomponents.dowloads.items.PopularTourItemShimmer

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                PopularTourItemShimmer()
            }
            uiState.isEmpty -> {
                FavoritesEmpty(modifier = Modifier.padding(paddingValues))
            }
            else -> {
                FavoritesContent(
                    paddingValues = paddingValues,
                    uiState = uiState,
                    onAction = viewModel::handleAction,
                    navHostController = navHostController
                )
            }
        }
    }
}

@Composable
private fun FavoritesEmpty(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Нет избранных",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "Добавляйте понравившиеся туры\nв избранное",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp, horizontal = 32.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun FavoritesContent(
    paddingValues: PaddingValues,
    uiState: FavoritesUiState,
    onAction: (FavoritesAction) -> Unit,
    navHostController: NavHostController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(uiState.tours, key = { it.id }) { tour ->
            PopularTourItem(
                tour = tour,
                onClick = { onAction(FavoritesAction.OnTourClick(tour.id)) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoritesEmptyPreview() {
    FavoritesEmpty()
}
