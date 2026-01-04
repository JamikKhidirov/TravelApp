package com.example.presentation.screens


import androidx.collection.longObjectMapOf
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.domain.data.citydata.CityDto
import com.example.presentation.uicomponents.tabrow.TabRowMainScreen
import com.example.presentation.uicomponents.topbars.MainTopAppBars
import com.example.presentation.uicomponents.vidjets.CityItem
import com.example.presentation.uicomponents.vidjets.TabRowItem
import viewmodals.MainViewModal


@Composable
@Preview(showBackground = true)
fun MainScreen(
    viewModel: MainViewModal = hiltViewModel()  // Исправлено: viewModel, не viewmodal
) {
    val cities = viewModel.citiesState.collectAsState()
    val loading = viewModel.loading.collectAsState()
    val errorMessage = viewModel.errorMessage.collectAsState()

    var tab by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            when{
                loading.value == true -> {
                    null
                }
                errorMessage.value != null -> {
                    null
                }
                cities.value != null -> {
                    MainTopAppBars(modifier = Modifier)
                }
            }
        }
    ) { paddingValues ->
        when {
            loading.value == true -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(64.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            errorMessage.value != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                        ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Ошибка",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Проверьте подключение к интернету",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        )
                    }
                }
            }
            else -> {
                BottomMainScreen(
                    paddingValues = paddingValues,
                    cities = cities.value,
                    onCLickCity = {cityDto ->

                    },
                    onClickTabCategory = { tabStr ->
                        tab = tabStr
                    }
                )
            }
        }
    }
}



@Composable
fun BottomMainScreen(
    paddingValues: PaddingValues,
    cities: List<CityDto>,
    onCLickCity: (CityDto) -> Unit = {},
    onClickTabCategory: (tab: String) -> Unit = {}
) {
    if (cities.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("Города не найдены")
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        stickyHeader {
            TabRowMainScreen(
                modifier = Modifier.padding(top = 2.dp),
                onClickTab = onClickTabCategory
            )
        }
        items(cities, key = {city -> city.id}) { city ->
            CityItem(
                city = city,
                onClickCityDto = onCLickCity
            )
        }
    }
}



