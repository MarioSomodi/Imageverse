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
        onSuccess : () -> Unit,
        onFail : () -> Unit,
        onSuccessHigherPrivileges : () -> Unit,
    ) : AuthenticationResponse {
        try {
            val authenticationResult : AuthenticationResponse =  withContext(Dispatchers.IO){
                imageverseApi.postLogin(LoginRequest(email, password))
            }
            if(authenticationResult.user == null) onFail()
            if(authenticationResult.user!!.isAdmin){
                onSuccessHigherPrivileges()
            }else{
                onSuccess()
            }
            return authenticationResult;
        }catch (e : Exception){
            onFail()
        }
        return AuthenticationResponse(null, null);
    }
}