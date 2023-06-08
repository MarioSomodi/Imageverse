package com.msomodi.imageverse.model.auth.google

data class GoogleUser (
    val id : String,
    val name : String?,
    val surname : String?,
    val email : String?,
    val profileImage : String?
)