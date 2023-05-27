package com.msomodi.imageverse.repository

import androidx.annotation.WorkerThread
import com.msomodi.imageverse.api.ImageverseApi
import com.msomodi.imageverse.db.ImageverseDatabase
import com.msomodi.imageverse.model.auth.request.LoginRequest
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val imageverseApi: ImageverseApi,
    private val imageverseDatabase: ImageverseDatabase
){
    suspend fun postLogin(
        email : String,
        password : String,
    ) : Result<AuthenticationResponse> {
        val result =  withContext(Dispatchers.IO){
            imageverseApi.postLogin(LoginRequest(email, password))
        }
        if(result.isSuccess){
            imageverseDatabase.authenticatedUsersDao().addAuthenticatedUser(result.getOrNull()!!)
        }
        return result;
    }
    suspend fun getAuthenticatedUser(
    ) : Flow<AuthenticationResponse?> {
        return imageverseDatabase.authenticatedUsersDao().getAuthenticatedUser()
    }
    suspend fun removeAuthenticatedUser(
    ) {
        imageverseDatabase.authenticatedUsersDao().deleteAuthenticatedUser()
    }
}