package com.example.home

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.home.action.HomeAction
import com.example.home.state.HomeUiState
import com.example.home.state.ui.PaginationState
import com.example.home.uicomponents.attractionsSection
import com.example.home.uicomponents.citiesSection
import com.example.home.uicomponents.toursSection
import com.example.home.viewmodel.HomeViewModel
import com.example.uikit.statescreen.LoadingScreen.HomeSkeletonScreen
import com.example.uikit.statescreen.NetWorkErrorScreen.NoInternetScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permission ->
        if (permission[Manifest.permission.ACCESS_FINE_LOCATION] == true
            || permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
        } else {
            Toast.makeText(context, "Без локации мы не найдем туры рядом", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    val isFirstLoading = uiState.isGlobalLoading &&
            uiState.citiesState.items.isEmpty() &&
            uiState.popularToursState.items.isEmpty()

    Scaffold(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "TravelApp",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                actions = {
                    IconButton(onClick = { navHostController.navigate(ScreenDestination.FavoritesScreen) }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Избранное")
                    }
                    IconButton(onClick = { navHostController.navigate(ScreenDestination.ProfileScreen) }) {
                        Icon(Icons.Default.Person, contentDescription = "Профиль")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        when {
            uiState.isGlobalLoading &&
                    uiState.citiesState.items.isEmpty() &&
                    uiState.attractionState.items.isEmpty() &&
                    uiState.popularToursState.items.isEmpty() -> {
                HomeSkeletonScreen()
            }
            uiState.error != null &&
                    uiState.citiesState.items.isEmpty() &&
                    uiState.attractionState.items.isEmpty() &&
                    uiState.popularToursState.items.isEmpty() -> {
                NoInternetScreen(onRetry = { viewModel.handleAction(HomeAction.Retry) })
            }
            else -> {
                HomeScreenContent(
                    uiState = uiState,
                    onAction = { action ->
                        when (action) {
                            is HomeAction.OnTourClick ->
                                navHostController.navigate(ScreenDestination.ProductDetail(action.value.id))
                            is HomeAction.OnCityClick ->
                                navHostController.navigate(ScreenDestination.AllProducts)
                            is HomeAction.OnAttractionClick ->
                                navHostController.navigate(ScreenDestination.AllProducts)
                            HomeAction.SeeAllAttractions ->
                                navHostController.navigate(ScreenDestination.AllProducts)
                            else -> viewModel.handleAction(action)
                        }
                    },
                    navHostController = navHostController
                )
            }
        }
    }
}

@Composable
fun HomeScreenContent(
    uiState: HomeUiState,
    onAction: (HomeAction) -> Unit,
    navHostController: NavHostController
) {
    val state = rememberLazyListState()

    OnBottomReached(state = state) {
        onAction(HomeAction.LoadMoreTours)
    }

    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            SearchBar(onClick = { navHostController.navigate(ScreenDestination.SearchScreen) })
        }

        item {
            Text(
                text = "Куда отправимся?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
            )
            Text(
                text = "Выбери направление и открой для себя новые места",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
            )
        }

        citiesSection(uiState, onAction)

        attractionsSection(uiState, onAction)

        toursSection(uiState, onAction)
    }
}

@Composable
private fun SearchBar(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Поиск городов и экскурсий...",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreenContent(
        uiState = HomeUiState(isGlobalLoading = false),
        onAction = {},
        navHostController = rememberNavController()
    )
}
