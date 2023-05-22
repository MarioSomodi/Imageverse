package com.msomodi.imageverse.model.auth.response

import com.msomodi.imageverse.util.LocalDateTimeAsStringSerializer
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class UserResponse (
    val id : String,
    val username : String,
    val name : String,
    val surname : String,
    val email : String,
    val profileImage : String,
    val packageId : String,
    val isAdmin : Boolean,
    val activePackageId : String,
    val postIds : Collection<String>,
    val userActionLogIds : Collection<String>,
    val userLimitIds : Collection<String>,
    val userStatistics : UserStatisticsResponse,
    @Serializable(LocalDateTimeAsStringSerializer::class)
    val packageValidFrom : LocalDateTime,
    val previousPackageId : String,
    val refreshToken : String,
    @Serializable(LocalDateTimeAsStringSerializer::class)
    val refreshTokenExpiry : LocalDateTime
)
