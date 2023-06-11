package com.msomodi.imageverse.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class UserEmailUpdateRequest(
    val id: String,
    val email: String,
    val authenticationType : Int
)
