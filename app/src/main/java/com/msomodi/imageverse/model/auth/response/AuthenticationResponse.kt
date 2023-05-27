package com.msomodi.imageverse.model.auth.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Entity(tableName = "authenticated_users_table")
@Serializable
data class AuthenticationResponse(
    @PrimaryKey(autoGenerate = true)
    @Transient
    val authenticatedUserId: Int = 0,
    val user : UserResponse?,
    val token : String
)