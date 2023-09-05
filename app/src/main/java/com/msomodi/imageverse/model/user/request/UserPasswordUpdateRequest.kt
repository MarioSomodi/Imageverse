package com.msomodi.imageverse.model.user.request

import kotlinx.serialization.Serializable

@Serializable
data class UserPasswordUpdateRequest(
    val id: String,
    val currentPassword: String,
    val newPassword: String
)
