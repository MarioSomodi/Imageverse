package com.msomodi.imageverse.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.msomodi.imageverse.model.post.response.PostResponse

@Dao
interface PostsDao {
    @Query("SELECT * FROM posts_table")
    fun getPosts() : PagingSource<Int, PostResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPosts(posts: List<PostResponse>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPost(post: PostResponse)

    @Query("DELETE FROM posts_table")
    suspend fun deletePosts()

    @Update
    suspend fun update(post: PostResponse)

    @Query("DELETE FROM posts_table WHERE postId = :postId")
    suspend fun deleteByPostId(postId: Int)
}