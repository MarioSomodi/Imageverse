package com.msomodi.imageverse.model.user.request

import kotlinx.serialization.Serializable

@Serializable
data class UserIsAdminUpdateRequest(
    val id: String,
    val isAdmin: Boolean
)
