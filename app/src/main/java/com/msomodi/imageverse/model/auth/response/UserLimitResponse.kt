package com.msomodi.imageverse.model.auth.response

import kotlinx.serialization.Serializable

@Serializable
data class UserLimitResponse (
    val amountOfMBUploaded : Int,
    val amountOfImagesUploaded : Int,
    val requestedChangeOfPackage : Boolean
)