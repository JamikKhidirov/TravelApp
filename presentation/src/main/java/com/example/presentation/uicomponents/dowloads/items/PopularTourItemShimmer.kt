package com.example.presentation.uicomponents.dowloads.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.uicomponents.dowloads.ShimmerPlaceholder


@Composable
fun PopularTourItemShimmer(){
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(
                horizontal = 15.dp,
                vertical = 8.dp
            ),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        repeat(5){
            ShimmerPlaceholder(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp),
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}