package com.example.presentation.uicomponents.vidjets.ImageVidjetDesc


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
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
import coil.request.ImageRequest
import com.example.domain.wegodata.citiesdata.City
import com.example.presentation.R


@Composable
fun Imagevidjet(
    modifier: Modifier,
    city: City,
    onClickCity: (City) -> Unit
){
    Card(
        modifier = modifier.fillMaxSize(),

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
                    .crossfade(true)    // Плавное появление
                    .build(),
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
                    text = city.country,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,

                )
        }
    }
}
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F0F0)
@Composable
fun ImagevidjetPreview() {
    // Создаем тестовые данные
    val mockCity = City(
        id = 1,
        name = "Париж",
        slug = "paris",
        preview = "https://wgt-prod-storage.s3.amazonaws.com/media/CACHE/images/world/attraction/4795/tmpz7s8s0gu/d48d08b802ab48574335007b0fffc401.jpg", // В превью картинка может не загрузиться без интернета
        itemsCount = 124,
        country = "Франция"
    )

    // Отображаем виджет
    MaterialTheme {
        Imagevidjet(
            modifier = Modifier
                .padding(16.dp)
                .width(250.dp)
                .height(400.dp), // Ограничиваем ширину для наглядности
            city = mockCity,
            onClickCity = { /* Действие при клике */ }
        )
    }
}