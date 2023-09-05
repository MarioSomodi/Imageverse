package com.msomodi.imageverse.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.msomodi.imageverse.dao.AuthenticatedUsersDao
import com.msomodi.imageverse.dao.PostRemoteKeysDao
import com.msomodi.imageverse.dao.PostsDao
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.model.post.PostRemoteKeys
import com.msomodi.imageverse.model.post.response.PostResponse

@Database(
    entities = [AuthenticationResponse::class, PostResponse::class, PostRemoteKeys::class],
    version = 4,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class ImageverseDatabase : RoomDatabase(){
    abstract fun authenticatedUsersDao() : AuthenticatedUsersDao
    abstract fun postsDao() : PostsDao
    abstract fun postRemoteKeysDao() : PostRemoteKeysDao
}