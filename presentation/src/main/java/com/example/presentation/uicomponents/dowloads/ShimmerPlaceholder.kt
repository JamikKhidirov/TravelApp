package com.example.presentation.uicomponents.dowloads

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.presentation.modifiers.shimerEffect


@Composable
fun ShimmerPlaceholder(
    modifier: Modifier,
    shape: Shape =  RoundedCornerShape(4.dp)
){
    Box(
        modifier = modifier
            .clip(shape)
            .shimerEffect() // Используем ваш существующий экстеншн
    )
}