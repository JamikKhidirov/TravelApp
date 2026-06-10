package com.example.pushing.data.model

data class FcmSendResponse(
    val multicast_id: Long? = null,
    val success: Int = 0,
    val failure: Int = 0,
    val canonical_ids: Int = 0,
    val results: List<FcmResult>? = null
)

data class FcmResult(
    val message_id: String? = null,
    val error: String? = null
)
