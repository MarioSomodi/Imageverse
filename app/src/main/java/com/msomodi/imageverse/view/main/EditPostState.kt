package com.msomodi.imageverse.view.main

data class EditPostState (
    val description : String = "",
    val isDescriptionValid : Boolean = true,
    val hashtags : Collection<String> = listOf(),
    val isHashtagsValid : Boolean = true,
)
