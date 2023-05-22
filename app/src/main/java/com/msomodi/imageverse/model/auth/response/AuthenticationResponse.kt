package com.msomodi.imageverse.model.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthenticationResponse(
    val user : UserResponse?,
    val token : String?
)