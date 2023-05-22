package com.msomodi.imageverse.model.auth.response

import com.msomodi.imageverse.util.LocalDateTimeAsStringSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserStatisticsResponse(
    val id : String,
    val totalMbUploaded : Int,
    val totalImagesUploaded : Int,
    val totalTimesUserRequestedPackageChange : Int,
    @Serializable(LocalDateTimeAsStringSerializer::class)
    val firstLogin : LocalDateTime,
    @Serializable(LocalDateTimeAsStringSerializer::class)
    val lastLogin : LocalDateTime,
    val totalTimesLoggedIn : Int,
    val totalTimesPostsWereEdited : Int
)