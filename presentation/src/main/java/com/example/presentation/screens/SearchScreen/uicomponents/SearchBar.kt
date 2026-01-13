package com.example.presentation.screens.SearchScreen.uicomponents

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sin


@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onTextValue: (String) -> Unit = {}
) {
    var textValue by remember { mutableStateOf("") }

    BasicTextField(
        value = textValue,
        onValueChange = {
            textValue = it
            onTextValue(it)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp), // Высота как у стандартного TextField
        singleLine = true,
        textStyle = TextStyle(fontSize = 19.sp),
        // Здесь мы настраиваем внешний вид через DecorationBox
        decorationBox = { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = textValue,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = PaddingValues(start = 20.dp, end = 10.dp),
                placeholder = {
                    Text(
                        text = "Город, экскурсия, билет, доставка",
                        fontSize = 19.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                trailingIcon = {
                    if (textValue.isNotEmpty()) {
                        IconButton(onClick = { textValue = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = null)
                        }
                    }
                },
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    )
}