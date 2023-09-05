package com.msomodi.imageverse.model.hashtag.response

import kotlinx.serialization.Serializable

@Serializable
data class HashtagResponse(
    val id : String,
    val name : String
)
