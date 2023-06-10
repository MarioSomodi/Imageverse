package com.msomodi.imageverse.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val expiredAccessToken : String,
    val refreshToken : String
)
