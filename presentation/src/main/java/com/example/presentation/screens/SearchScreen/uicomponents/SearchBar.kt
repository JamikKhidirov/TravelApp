package com.example.presentation.screens.SearchScreen.uicomponents

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin


@Composable
@Preview(showBackground = true)
fun SearchBar(
    modifier: Modifier = Modifier,
    onTextValue: (String) -> Unit = {}
){

    var textValue by remember { mutableStateOf("") }

    TextField(
        value = textValue,
        modifier = modifier,
        onValueChange = onTextValue,
        shape = RoundedCornerShape(15.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledContainerColor = MaterialTheme.colorScheme.surface
        ),

        placeholder = {
            Text(
                text = "Город, экскурсия, билет, доставка",
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.padding(start = 10.dp),
                fontSize = 19.sp,
                color = Color.Gray
            )
        },

        trailingIcon = {
            if (textValue.isNotEmpty()){
                IconButton(
                    modifier = Modifier.size(32.dp),
                    onClick = {
                        textValue = ""
                    }
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Иконка для очистки"
                    )
                }
            }
        }
    )
}