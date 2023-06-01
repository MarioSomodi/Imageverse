package com.msomodi.imageverse.repository

import com.msomodi.imageverse.api.ImageverseApi
import com.msomodi.imageverse.db.ImageverseDatabase
import com.msomodi.imageverse.model.auth.request.LoginRequest
import com.msomodi.imageverse.model.auth.request.RegisterRequest
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val _imageverseApi: ImageverseApi,
    private val _imageverseDatabase: ImageverseDatabase
){
    suspend fun postLogin(
        email : String,
        password : String,
    ) : Result<AuthenticationResponse> {
        val result =  withContext(Dispatchers.IO){
            _imageverseApi.postLogin(LoginRequest(email, password))
        }
        result.onSuccess {
            _imageverseDatabase.authenticatedUsersDao().deleteAuthenticatedUser()
            _imageverseDatabase.authenticatedUsersDao().addAuthenticatedUser(it)
        }
        return result;
    }

    suspend fun postRegister(
        registerRequest: RegisterRequest,
    ) : Result<AuthenticationResponse> {
        val result =  withContext(Dispatchers.IO){
            _imageverseApi.postRegister(registerRequest)
        }
        result.onSuccess {
            _imageverseDatabase.authenticatedUsersDao().deleteAuthenticatedUser()
            _imageverseDatabase.authenticatedUsersDao().addAuthenticatedUser(it)
        }
        return result;
    }

    fun getAuthenticatedUser(
    ) : Flow<AuthenticationResponse?> {
        return _imageverseDatabase.authenticatedUsersDao().getAuthenticatedUser()
    }
    suspend fun removeAuthenticatedUser(
    ) {
        _imageverseDatabase.authenticatedUsersDao().deleteAuthenticatedUser()
    }
}