package com.msomodi.imageverse.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoUpdateRequest (
    val id: String,
    val username: String,
    val name: String,
    val surname: String
)