package com.example.profile

import com.example.profile.state.ProfileUiState
import org.junit.Assert.assertNotNull
import org.junit.Test

class ProfileViewModelTest {

    @Test
    fun `initial state exists`() {
        val state = ProfileUiState()
        assertNotNull(state)
    }
}
