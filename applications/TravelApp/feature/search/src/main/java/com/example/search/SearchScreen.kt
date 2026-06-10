package com.example.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.common.ScreenDestination
import com.example.common.UiError
import com.example.network.wegodata.productpopular.Tour
import com.example.network.wegodata.searchdata.CityDetail
import com.example.search.action.SearchAction
import com.example.search.state.SearchUiState
import com.example.search.viewmodel.SearchViewModel
import com.example.uikit.statescreen.NetWorkErrorScreen.NoInternetScreen
import com.example.uikit.uicomponents.dowloads.ShimmerPlaceholder
import com.example.uikit.uicomponents.dowloads.items.PopularTourItemShimmer
import com.example.uikit.uicomponents.items.PopularTourItem

private val tabs = listOf("Все", "Города", "Экскурсии")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        keyboardController?.show()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) { paddingValues ->
        when {
            uiState.error != null && uiState.results.isEmpty() && uiState.query.isEmpty() -> {
                NoInternetScreen(onRetry = { viewModel.handleAction(SearchAction.ClearQuery) })
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    SearchBar(
                        query = uiState.query,
                        onQueryChange = { viewModel.handleAction(SearchAction.OnSearchQueryChange(it)) },
                        onClear = { viewModel.handleAction(SearchAction.ClearQuery) }
                    )

                    TabRow(selectedTab = selectedTab, onTabSelected = { selectedTab = it })

                    val filteredResults = when (selectedTab) {
                        1 -> uiState.results.filter { it.type == "city" }
                        2 -> uiState.results.filter { it.type == "product" || it.type == "attraction" }
                        else -> uiState.results
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        if (uiState.query.isEmpty()) {
                            if (uiState.isInitialLoading) {
                                item {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        repeat(3) { PopularTourItemShimmer() }
                                    }
                                }
                            } else {
                                item {
                                    Text(
                                        text = "Популярные города",
                                        modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                item {
                                    LazyRow(
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        items(uiState.popularTours.take(8)) { tour ->
                                            CityCard(
                                                name = tour.city.name,
                                                preview = tour.preview,
                                                onClick = { navigateTourDetail(navHostController, tour.id) }
                                            )
                                        }
                                    }
                                }
                                item {
                                    Spacer(Modifier.height(12.dp))
                                    Text(
                                        text = "Популярные туры",
                                        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                item {
                                    LazyRow(
                                        contentPadding = PaddingValues(horizontal = 16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        items(uiState.popularTours.take(8)) { tour ->
                                            TourCard(
                                                tour = tour,
                                                onClick = { navigateTourDetail(navHostController, tour.id) }
                                            )
                                        }
                                    }
                                }
                                items(uiState.popularTours) { tour ->
                                    PopularTourItem(
                                        tour = tour,
                                        onClick = { navigateTourDetail(navHostController, tour.id) }
                                    )
                                }
                            }
                        } else {
                            if (uiState.isLoading) {
                                item {
                                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
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
                            } else if (filteredResults.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                Icons.Default.Search,
                                                contentDescription = null,
                                                modifier = Modifier.size(64.dp),
                                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                            )
                                            Spacer(Modifier.height(16.dp))
                                            Text(
                                                text = "Ничего не найдено",
                                                fontSize = 18.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = "Попробуйте изменить запрос",
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                            )
                                        }
                                    }
                                }
                            } else {
                                items(filteredResults, key = { it.id }) { item ->
                                    CitySearchItem(
                                        city = item,
                                        onClick = { viewModel.handleAction(SearchAction.OnCityClick(item)) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TabRow(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        tabs.forEachIndexed { index, title ->
            FilterChip(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                label = { Text(title) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        placeholder = { Text("Поиск городов и экскурсий...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Default.Clear, contentDescription = "Очистить")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    )
}

@Composable
private fun CityCard(
    name: String,
    preview: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.width(140.dp).height(100.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(preview)
                    .crossfade(true)
                    .build(),
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name,
                    color = androidx.compose.ui.graphics.Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun TourCard(
    tour: Tour,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(tour.preview)
                    .crossfade(true)
                    .build(),
                contentDescription = tour.title,
                modifier = Modifier.fillMaxWidth().height(100.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = tour.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "${tour.price.toInt()} ${tour.currency}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
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
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(90.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(city.preview)
                    .crossfade(true)
                    .build(),
                contentDescription = city.displayName,
                modifier = Modifier.size(90.dp).clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = city.displayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = city.country?.name.orEmpty(),
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun navigateTourDetail(navHostController: NavHostController, tourId: Int) {
    navHostController.navigate(ScreenDestination.ProductDetail(id = tourId))
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    SearchScreen(navHostController = rememberNavController())
}
