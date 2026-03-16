package com.example.home.uikit

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
@Preview
fun NewItem(
    modifier: Modifier = Modifier
){
    Surface(
        modifier = modifier,
        shadowElevation = 5.dp,
        shape = RoundedCornerShape(30.dp)
    ) {
        Text(
            text = "Новинка",
            fontWeight = FontWeight(500),
            fontSize = 15.sp,
            modifier = Modifier
                .padding(
                    horizontal = 10.dp,
                    vertical = 3.dp
                )
        )
    }
}