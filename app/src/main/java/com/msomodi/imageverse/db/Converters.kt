package com.msomodi.imageverse.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.msomodi.imageverse.model.auth.response.UserResponse

class Converters {
    val gson = Gson();

    @TypeConverter
    fun toUserJson(user: UserResponse) : String {
        return gson.toJson(user)
    }

    @TypeConverter
    fun fromUserJson(json: String): UserResponse{
        return gson.fromJson(json, UserResponse::class.java)
    }
}