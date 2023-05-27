package com.msomodi.imageverse.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.msomodi.imageverse.dao.AuthenticatedUsersDao
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse

@Database(
    entities = [AuthenticationResponse::class],
    version = 2,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class ImageverseDatabase : RoomDatabase(){
    abstract fun authenticatedUsersDao() : AuthenticatedUsersDao
}