package com.msomodi.imageverse.view.auth

data class AuthenticationState (
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
)