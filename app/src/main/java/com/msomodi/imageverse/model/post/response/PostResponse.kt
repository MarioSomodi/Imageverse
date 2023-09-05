package com.msomodi.imageverse.model.post.response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.msomodi.imageverse.model.hashtag.response.HashtagResponse
import com.msomodi.imageverse.model.image.response.ImageResponse
import kotlinx.serialization.Serializable

@Entity(tableName = "posts_table")
@Serializable
data class PostResponse (
    @PrimaryKey(autoGenerate = true)
    @kotlinx.serialization.Transient
    val postId : Int = 0,
    val id : String,
    val description : String,
    val author : String,
    val authorPhoto : String,
    val userId : String,
    val postedAtDateTime : String,
    val updatedAtDateTime : String,
    val images : List<ImageResponse>,
    val hashtags : List<HashtagResponse>
)