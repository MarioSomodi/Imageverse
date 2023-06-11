package com.msomodi.imageverse.view.main.userProfile

data class ChangePasswordState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val isNewPasswordValid : Boolean = false
)
