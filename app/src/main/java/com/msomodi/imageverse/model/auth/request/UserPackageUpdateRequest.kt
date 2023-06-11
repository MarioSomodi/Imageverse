package com.msomodi.imageverse.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class UserPackageUpdateRequest(
    val id: String,
    val packageId: String
)
