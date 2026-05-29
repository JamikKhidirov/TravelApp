package com.example.notification

import com.example.notification.action.NotificationAction
import com.example.notification.state.NotificationUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationViewModelTest {

    @Test
    fun `initial state has empty fields`() {
        val state = NotificationUiState()
        assertEquals("", state.serverKey)
        assertEquals("", state.title)
        assertEquals("", state.body)
        assertEquals("", state.token)
        assertTrue(state.sendToAll)
        assertFalse(state.isSending)
        assertNull(state.resultMessage)
        assertNull(state.isSuccess)
    }

    @Test
    fun `onServerKeyChange updates server key`() {
        val state = NotificationUiState()
        val updated = state.copy(serverKey = "AAAA-test-key")
        assertEquals("AAAA-test-key", updated.serverKey)
    }

    @Test
    fun `onTitleChange updates title`() {
        val state = NotificationUiState()
        val updated = state.copy(title = "New tour")
        assertEquals("New tour", updated.title)
    }

    @Test
    fun `onBodyChange updates body`() {
        val state = NotificationUiState()
        val updated = state.copy(body = "Check out our new excursion")
        assertEquals("Check out our new excursion", updated.body)
    }

    @Test
    fun `onSendToAllChange toggles sendToAll`() {
        val state = NotificationUiState(sendToAll = true)
        val toggled = state.copy(sendToAll = false)
        assertFalse(toggled.sendToAll)
    }

    @Test
    fun `onTokenChange updates token`() {
        val state = NotificationUiState()
        val updated = state.copy(token = "device-token-123")
        assertEquals("device-token-123", updated.token)
    }

    @Test
    fun `sending state transitions correctly`() {
        val state = NotificationUiState()
        val sending = state.copy(isSending = true)
        assertTrue(sending.isSending)
        val done = sending.copy(isSending = false, resultMessage = "Sent!", isSuccess = true)
        assertFalse(done.isSending)
        assertEquals("Sent!", done.resultMessage)
        assertTrue(done.isSuccess!!)
    }

    @Test
    fun `clearResult resets result fields`() {
        val state = NotificationUiState(resultMessage = "Error", isSuccess = false)
        val cleared = state.copy(resultMessage = null, isSuccess = null)
        assertNull(cleared.resultMessage)
        assertNull(cleared.isSuccess)
    }
}
