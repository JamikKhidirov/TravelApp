package com.example.notification

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class NotificationScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun notificationScreen_showsTitle() {
        composeTestRule.setContent {
            NotifictionScreen()
        }

        composeTestRule.onNodeWithText("Push-уведомления").assertExists()
    }

    @Test
    fun notificationScreen_showsTargetOptions() {
        composeTestRule.setContent {
            NotifictionScreen()
        }

        composeTestRule.onNodeWithText("Все устройства").assertExists()
        composeTestRule.onNodeWithText("Конкретное устройство").assertExists()
    }

    @Test
    fun notificationScreen_showsSendButton() {
        composeTestRule.setContent {
            NotifictionScreen()
        }

        composeTestRule.onNodeWithText("Отправить").assertExists()
    }
}
