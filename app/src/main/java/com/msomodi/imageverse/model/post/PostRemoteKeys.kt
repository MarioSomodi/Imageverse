package com.msomodi.imageverse.model.post

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_remote_keys_table")
data class PostRemoteKeys(
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)
