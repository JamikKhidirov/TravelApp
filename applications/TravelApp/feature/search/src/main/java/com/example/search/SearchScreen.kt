package com.example.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.home.state.network.UiError
import com.example.home.uikit.PopularTourItem
import com.example.network.wegodata.productpopular.Tour
import com.example.network.wegodata.searchdata.CityDetail
import com.example.search.action.SearchAction
import com.example.search.state.SearchUiState
import com.example.search.viewmodel.SearchViewModel
import com.example.uikit.statescreen.NetWorkErrorScreen.NoInternetScreen
import com.example.uikit.uicomponents.dowloads.ShimmerPlaceholder
import com.example.uikit.uicomponents.dowloads.items.PopularTourItemShimmer

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        keyboardController?.show()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { paddingValues ->
        when {
            uiState.error != null && uiState.results.isEmpty() && uiState.query.isEmpty() -> {
                NoInternetScreen(onRetry = { viewModel.handleAction(SearchAction.ClearQuery) })
            }
            else -> {
                SearchScreenContent(
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
private fun SearchScreenContent(
    paddingValues: PaddingValues,
    uiState: SearchUiState,
    onAction: (SearchAction) -> Unit,
    navHostController: NavHostController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(top = 70.dp)
        ) {
            if (uiState.query.isEmpty()) {
                if (uiState.isInitialLoading) {
                    item { PopularTourItemShimmer() }
                } else if (uiState.popularTours.isNotEmpty()) {
                    item {
                        Text(
                            text = "Популярные туры",
                            modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 8.dp),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    items(uiState.popularTours, key = { it.id }) { tour ->
                        PopularTourItem(
                            tour = tour,
                            onClick = {}
                        )
                    }
                }
            } else {
                if (uiState.isLoading) {
                    item {
                        Column(modifier = Modifier.padding(horizontal = 15.dp, vertical = 8.dp)) {
                            repeat(5) {
                                ShimmerPlaceholder(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(80.dp)
                                        .padding(vertical = 4.dp),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }
                        }
                    }
                } else if (uiState.error != null) {
                    item {
                        Text(
                            text = "Ошибка загрузки",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else if (uiState.results.isEmpty()) {
                    item {
                        Text(
                            text = "Ничего не найдено",
                            modifier = Modifier.padding(16.dp),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    items(uiState.results, key = { it.id }) { city ->
                        CitySearchItem(
                            city = city,
                            onClick = { onAction(SearchAction.OnCityClick(city)) }
                        )
                    }
                }
            }
        }

        SearchBarVidjet(
            navHostController = navHostController,
            searchQuery = uiState.query,
            onSearchQueryChange = { onAction(SearchAction.OnSearchQueryChange(it)) }
        )
    }
}

@Composable
private fun CitySearchItem(
    city: CityDetail,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(80.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(city.preview)
                    .crossfade(true)
                    .build(),
                contentDescription = city.name,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(
                                androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.6f),
                                androidx.compose.ui.graphics.Color.Transparent
                            )
                        )
                    )
            )
            Text(
                text = "${city.name}, ${city.country.name}",
                modifier = Modifier.align(Alignment.CenterStart).padding(start = 12.dp),
                color = androidx.compose.ui.graphics.Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreen(navHostController = rememberNavController())
}
