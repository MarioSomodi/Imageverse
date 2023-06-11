package com.msomodi.imageverse.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class UserIsAdminUpdateRequest(
    val id: String,
    val isAdmin: Boolean
)
