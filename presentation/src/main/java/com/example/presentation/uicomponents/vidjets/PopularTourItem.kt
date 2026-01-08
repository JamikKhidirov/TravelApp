package com.example.presentation.uicomponents.vidjets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.node.TouchBoundsExpansion
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.wegodata.productpopular.Tour
import com.example.domain.wegodata.productpopular.TourAuthor
import com.example.domain.wegodata.productpopular.TourCity
import com.example.domain.wegodata.productpopular.TourTags


@Composable
fun PopularTourItem(
    tour: Tour ,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .height(400.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            AsyncImage(
                model = tour.preview ?: tour.cover,
                contentDescription = tour.title,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(200.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = tour.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "${tour.city.name} • ${tour.duration}",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Text(
                    text = "${tour.price.toInt()} ${tour.currency}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}



@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Popular tour item"
)
@Composable
fun PopularTourItemPreview() {
    val mockCity = TourCity(
        id = 1,
        name = "Санкт-Петербург",
        slug = "saint-petersburg"
    )

    val mockTags = TourTags(
        audioguide = true,
        available = true,
        hit = true,
        celebration = false,
        excursions = true,
        citys = true
    )

    val mockAuthor = TourAuthor(
        avatar = "https://picsum.photos/100/100",
        name = "Travel Guide",
        bio = "Лучший гид по ночному Петербургу",
        nickname = "night_guide"
    )

    val mockTour = Tour(
        id = 1,
        title = "Ночная экскурсия по крышам",
        slug = "night-roofs-tour",
        cover = "https://picsum.photos/400/300",
        preview = "https://picsum.photos/400/300?random=1",
        price = 4500.0,
        exprice = 5500.0,
        currency = "₽",
        currencyCode = "RUB",
        rating = 4.8,
        reviewsCount = 123,
        ratingsCount = 200,
        category = "Экскурсия",
        city = mockCity,
        duration = "3 часа",
        durationMin = 180,
        durationMax = 180,
        type = 1,
        tags = mockTags,
        locale = "ru",
        author = mockAuthor
    )

    MaterialTheme {
        PopularTourItem(
            tour = mockTour,
            onClick = {}
        )
    }
}
