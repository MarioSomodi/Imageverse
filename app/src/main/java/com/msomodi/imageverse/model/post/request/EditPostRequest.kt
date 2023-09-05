package com.msomodi.imageverse.model.post.request

import kotlinx.serialization.Serializable

@Serializable
data class EditPostRequest(
    val id : String,
    val description : String,
    val hashtags : Collection<String>
)
