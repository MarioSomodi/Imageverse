package com.msomodi.imageverse.view.auth

data class RegisterState (
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val surname: String = "",
    val username: String = "",
    val packageId : String = "",
    val isNameValid: Boolean = false,
    val isSurnameValid: Boolean = false,
    val isUsernameValid: Boolean = false,
    val isPackageIdValid: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
)