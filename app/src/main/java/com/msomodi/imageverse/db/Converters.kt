package com.msomodi.imageverse.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.msomodi.imageverse.model.hashtag.response.HashtagResponse
import com.msomodi.imageverse.model.image.response.ImageResponse
import com.msomodi.imageverse.model.user.response.UserResponse

class Converters {
    val gson = Gson();

    @TypeConverter
    fun toUserJson(user: UserResponse) : String {
        return gson.toJson(user)
    }

    @TypeConverter
    fun fromUserJson(json: String): UserResponse {
        return gson.fromJson(json, UserResponse::class.java)
    }

    @TypeConverter
    fun toListImagesJson(images: List<ImageResponse>) : String {
        return gson.toJson(images)
    }

    @TypeConverter
    fun fromListImagesJson(json: String): List<ImageResponse>{
        val itemType = object : TypeToken<List<ImageResponse>>(){}.type
        return gson.fromJson(json, itemType)
    }

    @TypeConverter
    fun toListHashtagJson(images: List<HashtagResponse>) : String {
        return gson.toJson(images)
    }

    @TypeConverter
    fun fromListHashtagJson(json: String): List<HashtagResponse>{
        val itemType = object : TypeToken<List<HashtagResponse>>(){}.type
        return gson.fromJson(json, itemType)
    }
}