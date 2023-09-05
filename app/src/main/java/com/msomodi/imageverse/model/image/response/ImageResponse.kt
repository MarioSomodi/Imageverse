package com.msomodi.imageverse.model.image.response

import kotlinx.serialization.Serializable

@Serializable
data class ImageResponse(
    val id : String,
    val name : String,
    val url : String,
    val size : String,
    val resolution : String,
    val format : String
)
