package com.example.presentation.uicomponents.vidjets.ImageVidjetDesc


import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest
import com.example.domain.model.DisplayableItem
import com.example.domain.wegodata.citiesdata.City
import com.example.presentation.R


@Composable
fun ImagevidjetGetCities(
    modifier: Modifier,
    city: DisplayableItem,
    onClickCity: (DisplayableItem) -> Unit
){
    Card(
        modifier = modifier
            .width(200.dp) // ОБЯЗАТЕЛЬНО: укажите ширину для LazyRow
            .height(260.dp),

        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 2.dp
        ),
        onClick = {
            onClickCity(city)
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ){
            // Загрузка изображения через Coil
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(city.preview) // URL изображения
                    .crossfade(true)
                    .build(),
                onError = {
                    Log.e("CoilError", "Ошибка загрузки: ${it.result.throwable.message}")
                    Log.e("CoilError", "Ошибка! URL: '${city.preview}'")
                    Log.e("CoilError", "Причина: ${it.result.throwable}")

                },
                contentDescription = "Photo of ${city.name}",
                placeholder = painterResource(R.drawable.ic_launcher_background),
                modifier = Modifier
                    .fillMaxSize()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )// Обрезка под размер контейнера


            // Контент с текстом
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = city.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "${city.itemsCount.toString()} экскурсий и билетов",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,

                )
        }
    }
}
}