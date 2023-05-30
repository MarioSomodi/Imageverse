package com.msomodi.imageverse.model.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class PackageResponse (
    val id : String,
    val name : String,
    val price : Double,
    val uploadSizeLimit : Int,
    val dailyUploadLimit : Int,
    val dailyImageUploadLimit : Int
)