package com.msomodi.imageverse.view.main.userProfile

data class EditUserInfoState(
    val username: String = "",
    val name: String = "",
    val surname: String = "",
    val isUsernameValid : Boolean = false,
    val isNameValid : Boolean = false,
    val isSurnameValid : Boolean = false
)
