package com.example.search.detail

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.network.wegodata.datareviews.Review
import com.example.network.wegodata.productdatailinfodata.AuthorInfo
import com.example.network.wegodata.productdatailinfodata.SimpleAttraction
import com.example.network.wegodata.productdatailinfodata.TourFullInfo
import com.example.network.wegodata.productdatailinfodata.TourImage
import com.example.search.viewmodel.TourDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TourDetailScreen(
    viewModel: TourDetailViewModel = hiltViewModel(),
    navHostController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                )
            )
        },
        bottomBar = {
            uiState.tour?.let { tour ->
                BottomBookingBar(tour = tour)
            }
        }
    ) { paddingValues ->
        when {
            uiState.isLoading && uiState.tour == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            uiState.error != null && uiState.tour == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Ошибка загрузки",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Повторить",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
            }
            uiState.tour != null -> {
                TourDetailContent(
                    tour = uiState.tour!!,
                    reviews = uiState.reviews,
                    isLoadingReviews = uiState.isLoadingReviews,
                    paddingValues = paddingValues
                )
            }
        }
    }
}

@Composable
private fun TourDetailContent(
    tour: TourFullInfo,
    reviews: List<Review>,
    isLoadingReviews: Boolean,
    paddingValues: PaddingValues
) {
    val hasImages = tour.images.isNotEmpty() || !tour.cover.isNullOrBlank()
    val hasDescription = !tour.description.isNullOrBlank()
    val hasHighlights = tour.highlights.isNotEmpty()
    val hasInclusions = tour.inclusions.isNotEmpty()
    val hasExclusions = tour.exclusions.isNotEmpty()
    val hasAttractions = tour.attractions.isNotEmpty()
    val hasAuthor = !tour.author?.name.isNullOrBlank()
    val hasAuthorAvatar = !tour.author?.avatar.isNullOrBlank()
    val hasDuration = !tour.duration.isNullOrBlank()
    val hasAddress = !tour.address.isNullOrBlank()
    val hasPrice = tour.price > 0

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        if (hasImages) {
            item { ImageCarousel(images = tour.images, cover = tour.cover) }
        }

        item { TourTitleSection(tour = tour) }

        item {
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
            )
            Spacer(Modifier.height(12.dp))
        }

        if (hasDuration || hasAddress || hasAuthor) {
            item {
                InfoChips(
                    duration = tour.duration,
                    address = tour.address,
                    authorName = tour.author?.name.orEmpty()
                )
            }
            item {
                Spacer(Modifier.height(16.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                )
                Spacer(Modifier.height(12.dp))
            }
        }

        if (hasDescription) {
            item { DescriptionSection(description = tour.description) }
        }

        if (hasHighlights) {
            item { SectionSpacer() }
            item { HighlightsSection(highlights = tour.highlights) }
        }

        if (hasInclusions) {
            item { SectionSpacer() }
            item {
                InclusionsSection(
                    title = "Включено",
                    items = tour.inclusions,
                    icon = Icons.Default.CheckCircle,
                    iconTint = Color(0xFF4CAF50)
                )
            }
        }

        if (hasExclusions) {
            item { Spacer(Modifier.height(12.dp)) }
            item {
                InclusionsSection(
                    title = "Не включено",
                    items = tour.exclusions,
                    icon = Icons.Default.Close,
                    iconTint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
        }

        if (hasAuthor) {
            item { SectionSpacer() }
            item { AuthorSection(author = tour.author!!, hasAvatar = hasAuthorAvatar) }
        }

        if (hasAttractions) {
            item { SectionSpacer() }
            item { AttractionsSection(attractions = tour.attractions) }
        }

        item { SectionSpacer() }
        item {
            ReviewsSection(
                reviews = reviews,
                isLoading = isLoadingReviews,
                tour = tour
            )
        }

        item { Spacer(Modifier.height(24.dp)) }
    }
}

@Composable
private fun SectionSpacer() {
    Spacer(Modifier.height(16.dp))
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 20.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    )
    Spacer(Modifier.height(12.dp))
}

@Composable
private fun ImageCarousel(
    images: List<TourImage>,
    cover: String
) {
    val displayUrls = if (images.isNotEmpty()) {
        images.map { if (!it.full.isNullOrBlank()) it.full else it.preview }
    } else if (!cover.isNullOrBlank()) {
        listOf(cover)
    } else {
        emptyList()
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        if (displayUrls.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(displayUrls.first())
                    .crossfade(true)
                    .build(),
                contentDescription = "Фото",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Нет фото",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background)
                    )
                )
        )

        if (displayUrls.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(displayUrls.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (index == 0) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == 0) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun TourTitleSection(tour: TourFullInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = tour.title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 30.sp
        )

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(5) { index ->
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < 4) Color(0xFFFFB300)
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(
                text = "4.0",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.width(4.dp))
            val authorName = tour.author?.name.orEmpty()
            Text(
                text = if (authorName.isNotEmpty()) "(отзывов: $authorName)" else "",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
private fun InfoChips(
    duration: String,
    address: String,
    authorName: String
) {
    val chips = mutableListOf<Pair<ImageVector, String>>()
    if (!duration.isNullOrBlank()) chips.add(Icons.Default.AccessTime to duration)
    if (!address.isNullOrBlank()) chips.add(Icons.Default.LocationOn to address)
    if (!authorName.isNullOrBlank()) chips.add(Icons.Default.Person to authorName)

    if (chips.isEmpty()) return

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(chips.size) { index ->
            val (icon, text) = chips[index]
            InfoChip(icon = icon, text = text)
        }
    }
}

@Composable
private fun InfoChip(icon: ImageVector, text: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                text = text,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun DescriptionSection(description: String) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        SectionTitle("Описание")
        Spacer(Modifier.height(8.dp))
        Text(
            text = description,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            lineHeight = 22.sp,
            maxLines = 6,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun HighlightsSection(highlights: List<String>) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        SectionTitle("Что вас ждёт")
        Spacer(Modifier.height(8.dp))
        highlights.take(5).forEach { highlight ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = highlight,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 22.sp
                )
            }
        }
        if (highlights.size > 5) {
            Text(
                "и ещё ${highlights.size - 5}...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 17.dp, top = 4.dp)
            )
        }
    }
}

@Composable
private fun InclusionsSection(
    title: String,
    items: List<String>,
    icon: ImageVector,
    iconTint: Color
) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        SectionTitle(title)
        Spacer(Modifier.height(8.dp))
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(10.dp))
                Text(
                    text = item,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun AuthorSection(
    author: AuthorInfo,
    hasAvatar: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (hasAvatar) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(author.avatar)
                    .crossfade(true)
                    .build(),
                contentDescription = author.name,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = author.name.take(1).uppercase(),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(Modifier.width(14.dp))
        Column {
            if (!author.bio.isNullOrBlank()) {
                Text(
                    text = author.bio,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            } else {
                Text(
                    text = "Гид",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
            Text(
                text = author.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
            if (!author.nickname.isNullOrBlank()) {
                Text(
                    text = author.nickname,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun AttractionsSection(attractions: List<SimpleAttraction>) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        SectionTitle("Достопримечательности")
        Spacer(Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(attractions.take(5)) { attraction ->
                val hasAttractionPreview = !attraction.preview.isNullOrBlank()
                Card(
                    modifier = Modifier.width(140.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    if (hasAttractionPreview) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(attraction.preview)
                                .crossfade(true)
                                .build(),
                            contentDescription = attraction.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = attraction.name.take(1).uppercase(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                            )
                        }
                    }
                    Text(
                        text = attraction.name,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(8.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun ReviewsSection(
    reviews: List<Review>,
    isLoading: Boolean,
    tour: TourFullInfo
) {
    val hasReviews = reviews.isNotEmpty()

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SectionTitle("Отзывы")
            if (hasReviews) {
                Text(
                    text = "${reviews.size} отзывов",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        Spacer(Modifier.height(8.dp))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterHorizontally),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.primary
            )
        } else if (!hasReviews) {
            Text(
                "Пока нет отзывов",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        } else {
            reviews.take(3).forEach { review ->
                ReviewCard(review = review)
                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ReviewCard(review: Review) {
    val hasAvatar = !review.avatar.isNullOrBlank()
    val hasText = !review.text.isNullOrBlank()
    val hasName = !review.name.isNullOrBlank()
    val hasDate = !review.date.isNullOrBlank()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (hasAvatar) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(review.avatar)
                            .crossfade(true)
                            .build(),
                        contentDescription = review.name,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else if (hasName) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = review.name.take(1).uppercase(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    if (hasName) {
                        Text(
                            text = review.name,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp
                        )
                    }
                    if (hasDate) {
                        Text(
                            text = review.date,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
                val rating = review.rating.coerceIn(0, 5)
                Row {
                    repeat(5) { index ->
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = if (index < rating) Color(0xFFFFB300)
                            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            if (hasText) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = review.text,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    lineHeight = 20.sp,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun BottomBookingBar(tour: TourFullInfo) {
    val hasPrice = tour.price > 0
    val currency = if (!tour.currency.isNullOrBlank()) tour.currency else "₽"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (hasPrice) {
                Column {
                    Text(
                        text = "${tour.price.toInt()} $currency",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (!tour.duration.isNullOrBlank()) {
                        Text(
                            text = "за человека",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
            }
            Button(
                onClick = { /* Booking action */ },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    if (hasPrice) "Забронировать" else "Уточнить цену",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}
