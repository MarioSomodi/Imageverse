package com.msomodi.imageverse.model.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(
    val accessToken : String
)
