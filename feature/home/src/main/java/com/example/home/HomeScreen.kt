package com.example.home

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.common.ScreenDestination
import com.example.home.action.HomeAction
import com.example.home.state.HomeUiState
import com.example.home.state.network.UiError
import com.example.home.state.ui.PaginationState
import com.example.home.uicomponents.attractionsSection
import com.example.home.uicomponents.citiesSection
import com.example.home.uicomponents.toursSection
import com.example.home.viewmodel.HomeViewModel
import com.example.uikit.statescreen.LoadingScreen.HomeSkeletonScreen
import com.example.uikit.statescreen.NetWorkErrorScreen.NoInternetScreen
import com.example.uikit.uicomponents.bars.BottomBarCastom
import com.example.uikit.uicomponents.search.SearchCard
import com.example.uikit.uicomponents.vidjets.TabRefresh


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navHostController: NavHostController
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(0) }

    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {permission ->
        if (permission[Manifest.permission.ACCESS_FINE_LOCATION] == true
            || permission[Manifest.permission.ACCESS_COARSE_LOCATION] == true){
            //Разрешение на точную геолокацию выдан вытвскиваем геолокацию
        }
        else {
            Toast.makeText(context, "Без локации мы не найдем туры рядом", Toast.LENGTH_LONG).show()

        }
    }


    LaunchedEffect(
        Unit
    ) {
       permissionLauncher.launch(
           arrayOf(
               Manifest.permission.ACCESS_FINE_LOCATION,
               Manifest.permission.ACCESS_COARSE_LOCATION
           )
       )
    }


    val state = rememberLazyListState()

    val isFirstLoading = uiState.isGlobalLoading &&
            uiState.citiesState.items.isEmpty() &&
            uiState.popularToursState.items.isEmpty()


    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background),

        bottomBar = {
            BottomBarCastom(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 15.dp)
                    .padding(bottom = 25.dp),
                currentTab = currentTab,
                onTabSelected = {newTab ->
                    currentTab = newTab
                }
            )
        }
    ) {paddingValues ->

        when{
            uiState.isGlobalLoading &&
                    uiState.citiesState.items.isEmpty() &&
                    uiState.popularToursState.items.isEmpty() -> {
                HomeSkeletonScreen()
            }

            uiState.error == UiError.NoInternet -> {
                NoInternetScreen(onRetry = {
                    viewModel.handleAction(HomeAction.Retry)
                })
            }

            else -> {
                BottomHomeScreen(
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
fun BottomHomeScreen(
    uiState: HomeUiState,
    paddingValues: PaddingValues,
    onAction: (HomeAction) -> Unit,
    navHostController: NavHostController
) {
    val state = rememberLazyListState()

    // Слушаем конец списка для туров
    OnBottomReached(state = state) {
        onAction(HomeAction.LoadMoreTours)
    }

    LazyColumn(
        state = state,
        modifier = Modifier.fillMaxSize(),
        contentPadding = paddingValues
    ) {
        // 1. Поиск
        stickyHeader {
            SearchCard(
                modifier = Modifier
                    .statusBarsPadding()
                    ,
                onClickSeacrCard = {
                    navHostController.navigate(ScreenDestination.SearchScreen)
                }
            )
        }

        // 2. Табы
        item {
            TabRefresh(
                onItemSelected = { tab ->
                    onAction(HomeAction.ChangeTab(isPopular = tab == "Популярные"))
                }
            )
        }

        // Секция Городов
        citiesSection(uiState, onAction)

        //Секция Достопримечательностей
        attractionsSection(uiState, onAction)

        //Вертикальный список Туров
        toursSection(uiState, onAction)
    }
}


@Composable
@Preview(showBackground = true)
fun HomeScreenWithScaffoldPreview() {
    val fakeUiState = HomeUiState(
        citiesState = PaginationState(items = FakeData.fakeCities),
        attractionState = PaginationState(items = FakeData.fakeAttractions),
        popularToursState = PaginationState(items = FakeData.fakeTours),
        isPopularTab = true,
        error = null,
        isGlobalLoading = false
    )

    var currentTab by remember { mutableStateOf(0) }

    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        bottomBar = {
            BottomBarCastom(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 20.dp),
                currentTab = currentTab,
                onTabSelected = {newTab ->
                    currentTab = newTab
                }
            )
        }
    ) { paddingValues ->
        BottomHomeScreen(
            uiState = fakeUiState,
            paddingValues = paddingValues,
            onAction = {},
            navHostController = navController
        )
    }
}
