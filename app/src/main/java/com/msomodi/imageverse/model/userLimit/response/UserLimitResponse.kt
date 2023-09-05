package com.msomodi.imageverse.model.userLimit.response

import kotlinx.serialization.Serializable

@Serializable
data class UserLimitResponse (
    val amountOfMBUploaded : Double,
    val amountOfImagesUploaded : Int,
    val requestedChangeOfPackage : Boolean
)