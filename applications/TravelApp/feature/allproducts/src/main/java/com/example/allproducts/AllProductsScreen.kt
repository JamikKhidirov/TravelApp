package com.example.allproducts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.allproducts.action.AllProductsAction
import com.example.allproducts.state.AllProductsUiState
import com.example.allproducts.viewmodel.AllProductsViewModel
import com.example.home.OnBottomReached
import com.example.uikit.statescreen.LoadingScreen.HomeSkeletonScreen
import com.example.uikit.statescreen.NetWorkErrorScreen.NoInternetScreen
import com.example.uikit.uicomponents.items.PopularTourItem
import com.example.uikit.uicomponents.topbars.BackTopAppBar

@Composable
fun AllProductsScreen(
    viewModel: AllProductsViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    OnBottomReached(state = listState, buffer = 2) {
        viewModel.handleAction(AllProductsAction.LoadMore)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        topBar = {
            BackTopAppBar(
                title = "Все туры",
                onBackClick = { navHostController.popBackStack() }
            )
        }
    ) { paddingValues ->
        when {
            uiState.isGlobalLoading && uiState.toursState.items.isEmpty() -> {
                HomeSkeletonScreen(paddingValues = paddingValues)
            }
            uiState.error != null && uiState.toursState.items.isEmpty() -> {
                NoInternetScreen(onRetry = { viewModel.handleAction(AllProductsAction.Retry) })
            }
            else -> {
                AllProductsContent(
                    paddingValues = paddingValues,
                    uiState = uiState,
                    onAction = viewModel::handleAction
                )
            }
        }
    }
}

@Composable
private fun AllProductsContent(
    paddingValues: PaddingValues,
    uiState: AllProductsUiState,
    onAction: (AllProductsAction) -> Unit
) {
    val toursState = uiState.toursState

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(toursState.items, key = { it.id }) { tour ->
            PopularTourItem(
                tour = tour,
                onClick = { onAction(AllProductsAction.OnTourClick(tour)) }
            )
        }

        if (toursState.isLoading && toursState.items.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    androidx.compose.material3.CircularProgressIndicator(
                        color = androidx.compose.ui.graphics.Color(0XFFFF8C00)
                    )
                }
            }
        }

        if (toursState.items.isEmpty() && toursState.isLoading) {
            item {
                com.example.uikit.uicomponents.dowloads.items.PopularTourItemShimmer()
            }
        }
    }
}
