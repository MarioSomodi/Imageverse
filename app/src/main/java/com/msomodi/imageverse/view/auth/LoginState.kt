package com.msomodi.imageverse.view.auth

data class LoginState (
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
)