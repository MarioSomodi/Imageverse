package com.msomodi.imageverse.view.main

data class AddPostState(
    val description: String = "",
    val base64Image: String = "",
    val saveImageAs: String = "png",
    val hashtags : Collection<String> = listOf(),
    val isDescriptionValid : Boolean = false,
    val isBase64ImageValid : Boolean = false,
    val isSaveImageValid : Boolean = true,
    val isHashtagsValid :  Boolean = false
)
