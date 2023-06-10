package com.msomodi.imageverse.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface AuthenticatedUsersDao {
    @Query("SELECT * FROM authenticated_users_table LIMIT 1")
    fun getAuthenticatedUser() : Flow<AuthenticationResponse?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAuthenticatedUser(authenticatedUser : AuthenticationResponse)

    @Query("UPDATE authenticated_users_table SET token=:newToken WHERE authenticatedUserId = :authenticatedUserId")
    suspend fun updateToken(authenticatedUserId: Int, newToken : String)

    @Query("DELETE FROM authenticated_users_table")
    suspend fun deleteAuthenticatedUser()
}