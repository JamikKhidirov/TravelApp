package com.example.uikit.uicomponents.buttons

import android.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
@Preview(showBackground = true)
fun MainButton(
    modifier: Modifier = Modifier,
    textButton: String = "Показать все",
    onClickButton: () -> Unit = {}
){

    Button(
        modifier = modifier,
        onClick = onClickButton,
        shape = RoundedCornerShape(15.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0XFFFF8C00)
        )
    ) {
        Text(
            text = textButton,
            fontSize = 19.sp,
            color = Color.White,
            modifier = Modifier.padding(vertical = 10.dp)
        )
    }
}
