package com.msomodi.imageverse.model.exception

data class ApiException(
    val type: String,
    val title: String,
    val status : String,
)
