package com.msomodi.imageverse.model.user.response

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
    val packageValidFrom : String,
    val previousPackageId : String,
    val refreshToken : String,
    val refreshTokenExpiry : String,
    val authenticationType : Int
)
