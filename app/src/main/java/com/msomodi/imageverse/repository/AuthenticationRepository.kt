package com.msomodi.imageverse.repository

import com.msomodi.imageverse.api.ImageverseApi
import com.msomodi.imageverse.model.auth.request.LoginRequest
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val imageverseApi: ImageverseApi
){
    suspend fun postLogin(
        email : String,
        password : String,
    ) : Result<AuthenticationResponse> {
        val result =  withContext(Dispatchers.IO){
            imageverseApi.postLogin(LoginRequest(email, password))
        }
        return result;
    }
}