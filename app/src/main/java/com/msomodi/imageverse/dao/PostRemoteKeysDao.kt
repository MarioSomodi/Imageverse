package com.msomodi.imageverse.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.msomodi.imageverse.model.post.PostRemoteKeys

@Dao
interface PostRemoteKeysDao {
    @Query("SELECT * FROM post_remote_keys_table WHERE id=:id")
    suspend fun getPostRemoteKeys(id: String) : PostRemoteKeys

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPostRemoteKeys(postRemoteKeys: List<PostRemoteKeys>)

    @Query("DELETE FROM post_remote_keys_table")
    suspend fun deletePostRemoteKeys()
}