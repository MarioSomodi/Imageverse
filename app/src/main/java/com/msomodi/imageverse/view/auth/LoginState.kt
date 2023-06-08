package com.msomodi.imageverse.view.auth

data class LoginState (
    val email: String = "",
    val password: String = "",
    val authenticationProviderId : String? = null,
    val authenticationType : Int = 0,
    val isEmailValid: Boolean = false,
)