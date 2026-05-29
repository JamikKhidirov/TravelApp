package com.example.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.notification.action.NotificationAction
import com.example.notification.state.NotificationUiState
import com.example.notification.viewmodel.NotificationViewModel
import com.example.uikit.bars.PushTopBar

@Composable
fun NotifictionScreen(
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Surface(shadowElevation = 10.dp) {
                PushTopBar(modifier = Modifier.fillMaxWidth(), barText = "Push-уведомления")
            }
        }
    ) { innerPadding ->
        NotificationContent(
            modifier = Modifier.padding(innerPadding),
            uiState = uiState,
            onAction = viewModel::handleAction
        )
    }
}

@Composable
private fun NotificationContent(
    modifier: Modifier = Modifier,
    uiState: NotificationUiState,
    onAction: (NotificationAction) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Настройки FCM",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.serverKey,
            onValueChange = { onAction(NotificationAction.OnServerKeyChange(it)) },
            label = { Text("Server Key") },
            placeholder = { Text("AAAA...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Содержимое уведомления",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.title,
            onValueChange = { onAction(NotificationAction.OnTitleChange(it)) },
            label = { Text("Заголовок") },
            placeholder = { Text("Новая экскурсия") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.body,
            onValueChange = { onAction(NotificationAction.OnBodyChange(it)) },
            label = { Text("Текст уведомления") },
            placeholder = { Text("Откройте новую экскурсию...") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Получатель",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = uiState.sendToAll,
                onClick = { onAction(NotificationAction.OnSendToAllChange(true)) },
                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF8C00))
            )
            Text("Все устройства", modifier = Modifier.padding(start = 4.dp))
            Spacer(modifier = Modifier.width(24.dp))
            RadioButton(
                selected = !uiState.sendToAll,
                onClick = { onAction(NotificationAction.OnSendToAllChange(false)) },
                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFF8C00))
            )
            Text("Конкретное устройство", modifier = Modifier.padding(start = 4.dp))
        }

        if (!uiState.sendToAll) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.token,
                onValueChange = { onAction(NotificationAction.OnTokenChange(it)) },
                label = { Text("FCM Token устройства") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onAction(NotificationAction.Send) },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8C00)),
            enabled = !uiState.isSending
        ) {
            if (uiState.isSending) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.height(24.dp).width(24.dp)
                )
            } else {
                Text("Отправить", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }

        uiState.resultMessage?.let { message ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (uiState.isSuccess == true)
                        Color(0xFF4CAF50).copy(alpha = 0.15f)
                    else
                        Color(0xFFF44336).copy(alpha = 0.15f)
                )
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(16.dp),
                    color = if (uiState.isSuccess == true) Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NotificationScreenPreview() {
    NotifictionScreen()
}
