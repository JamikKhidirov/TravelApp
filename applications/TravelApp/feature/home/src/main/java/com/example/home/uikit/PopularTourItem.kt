package com.example.home.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.network.wegodata.citiesdata.City
import com.example.network.wegodata.productpopular.Tour
import com.example.network.wegodata.productpopular.TourAuthor
import com.example.network.wegodata.productpopular.TourTags


@Composable
fun PopularTourItem(
    modifier: Modifier = Modifier,
    tour: Tour,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()// Чуть шире, чем города, так как информации больше
            .height(380.dp)
            .padding(vertical = 10.dp)
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(24.dp), // Более мягкие углы
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 1. Изображение на весь фон
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(tour.preview ?: tour.cover)
                    .crossfade(true)
                    .build(),
                contentDescription = tour.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 2. Градиентная подложка, чтобы текст всегда читался
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.1f),
                                Color.Black.copy(alpha = 0.8f)
                            ),
                            startY = 300f // Градиент начинается ближе к низу
                        )
                    )
            )

            // 3. Контент поверх изображения
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Верхняя часть: Рейтинг или бейдж (если есть)
                if (tour.rating != null){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Surface(
                            color = Color.White.copy(alpha = 0.9f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "⭐ ${tour.rating}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }

                }

                else {
                    /*
                    Если рейтинг равен null тоесть если оценки нет мы говорим что
                    экскурсия новая
                    */
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        NewItem()
                    }
                }
                // Нижняя часть: Инфо о туре
                Column {
                    Text(
                        text = tour.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Город: ${tour.city.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        fontSize = 15.sp,
                    )

                    Text(
                        text = "Продолжительность: ${tour.duration}",
                        color = Color.White,
                        fontSize = 13.sp,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${tour.price.toInt()} ${tour.currency}",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold
                        )
                        if (tour.exprice > tour.price) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${tour.exprice.toInt()}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    textDecoration = TextDecoration.LineThrough
                                ),
                                color = Color.White.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E3A8A)
@Composable
private fun PopularTourItemPreviewWithRating() {
    MaterialTheme {
        PopularTourItem(
            modifier = Modifier,
            tour = Tour(
                id = 1,
                title = "Ночной тур по Москве-реке с шампанским",
                slug = "nochnoy-tur-moskva-reke",
                cover = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400",
                preview = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=400",
                price = 2500.0,
                exprice = 3500.0,
                currency = "₽",
                currencyCode = "RUB",
                rating = 4.8,
                reviewsCount = 127,
                ratingsCount = 156,
                category = "Ночная экскурсия",
                city = City(
                    id = 1,
                    name = "Москва",
                    slug = "moscow",
                    preview = "https://example.com/moscow.jpg",
                    itemsCount = 45,
                    country = "Россия"
                ),
                duration = "2 часа 30 мин",
                durationMin = 120,
                durationMax = 150,
                type = 1,
                tags = TourTags(
                    audioguide = true,
                    hit = true,
                    excursions = true
                ),
                locale = "ru",
                author = TourAuthor(
                    avatar = "https://example.com/author.jpg",
                    name = "Анна Петрова",
                    bio = "Профессиональный гид по Москве",
                    nickname = "@moscow_guide"
                )
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E3A8A)
@Composable
private fun PopularTourItemPreviewWithoutRating() {
    MaterialTheme {
        PopularTourItem(
            modifier = Modifier,
            tour = Tour(
                id = 2,
                title = "Экскурсия по Кремлю с гидом",
                slug = "ekskursiya-kreml",
                cover = "https://images.unsplash.com/photo-1543782249-3c9a0a8f07bd?w=400",
                preview = "https://images.unsplash.com/photo-1543782249-3c9a0a8f07bd?w=400",
                price = 1500.0,
                exprice = 0.0,
                currency = "₽",
                currencyCode = "RUB",
                rating = null,  // ❌ Нет рейтинга
                reviewsCount = 0,
                ratingsCount = 0,
                category = "Историческая экскурсия",
                city = City(
                    id = 1,
                    name = "Москва",
                    slug = "moscow",
                    preview = "https://example.com/moscow.jpg",
                    itemsCount = 45,
                    country = "Россия"
                ),
                duration = "1 час 45 мин",
                durationMin = 90,
                durationMax = 120,
                type = 2,
                tags = TourTags(
                    celebration = true,
                    citys = true
                ),
                locale = "ru",
                author = TourAuthor(
                    name = "Иван Сидоров",
                    nickname = "@kreml_guide"
                )
            ),
            onClick = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1E3A8A)
@Composable
private fun PopularTourItemPreviews() {
    MaterialTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "С рейтингом ⭐",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            PopularTourItemPreviewWithRating()

            Text(
                text = "НОВИНКА ✨",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            PopularTourItemPreviewWithoutRating()
        }
    }
}
