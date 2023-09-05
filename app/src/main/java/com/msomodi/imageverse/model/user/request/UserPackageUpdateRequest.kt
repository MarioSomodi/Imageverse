package com.msomodi.imageverse.model.user.request

import kotlinx.serialization.Serializable

@Serializable
data class UserPackageUpdateRequest(
    val id: String,
    val packageId: String
)
