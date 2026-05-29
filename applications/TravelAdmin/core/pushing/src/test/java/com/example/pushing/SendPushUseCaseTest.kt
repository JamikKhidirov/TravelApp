package com.example.pushing

import com.example.pushing.data.model.FcmSendRequest
import com.example.pushing.domain.FcmPushResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SendPushUseCaseTest {

    @Test
    fun `FcmSendRequest has correct structure for all devices`() {
        val request = FcmSendRequest(
            to = "/topics/all",
            notification = com.example.pushing.data.model.FcmNotification(
                title = "Test Title",
                body = "Test Body"
            )
        )
        assertEquals("/topics/all", request.to)
        assertEquals("Test Title", request.notification.title)
        assertEquals("Test Body", request.notification.body)
    }

    @Test
    fun `FcmSendRequest has correct structure for specific device`() {
        val token = "device-token-123"
        val request = FcmSendRequest(
            to = token,
            notification = com.example.pushing.data.model.FcmNotification(
                title = "Test",
                body = "Test body"
            )
        )
        assertEquals(token, request.to)
    }

    @Test
    fun `FcmPushResult Success is correctly created`() {
        val result: FcmPushResult = FcmPushResult.Success
        assertTrue(result is FcmPushResult.Success)
    }

    @Test
    fun `FcmPushResult Error has message`() {
        val result: FcmPushResult = FcmPushResult.Error("Failed")
        assertTrue(result is FcmPushResult.Error)
        assertEquals("Failed", (result as FcmPushResult.Error).message)
    }
}
