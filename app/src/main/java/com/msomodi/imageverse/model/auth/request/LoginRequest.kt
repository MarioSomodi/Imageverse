package com.msomodi.imageverse.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email : String,
    val password : String
)