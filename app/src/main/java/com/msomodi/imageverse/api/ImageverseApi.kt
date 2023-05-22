package com.msomodi.imageverse.api

import com.msomodi.imageverse.model.auth.request.LoginRequest
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ImageverseApi {
    @POST("/Authentication/Login")
    suspend fun postLogin(@Body loginRequest : LoginRequest) : AuthenticationResponse
}