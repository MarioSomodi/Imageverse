package com.msomodi.imageverse.model.user.response

import com.msomodi.imageverse.util.LocalDateTimeAsStringSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserStatisticsResponse(
    val id : String,
    val totalMbUploaded : Double,
    val totalImagesUploaded : Int,
    val totalTimesUserRequestedPackageChange : Int,
    val firstLogin : String,
    val lastLogin : String,
    val totalTimesLoggedIn : Int,
    val totalTimesPostsWereEdited : Int
)