package com.msomodi.imageverse.model.auth.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val username : String,
    val name : String,
    val surname : String,
    val email : String,
    val password : String,
    val packageId : String,
    val profileImage : String?,
    val authenticationProviderId : String?,
    val AuthenticationType : Int
)
