package com.msomodi.imageverse.model.post.request

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val userId : String,
    val description : String,
    val base64Image : String,
    val hashtags : Collection<String>,
    val saveImageAs : String,
)
