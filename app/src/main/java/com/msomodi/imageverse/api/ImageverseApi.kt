package com.msomodi.imageverse.api

import com.msomodi.imageverse.model.auth.request.LoginRequest
import com.msomodi.imageverse.model.auth.request.RegisterRequest
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.model.auth.response.PackageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ImageverseApi {
    @POST("/Authentication/Login")
    suspend fun postLogin(@Body loginRequest : LoginRequest) : Result<AuthenticationResponse>
    @POST("/Authentication/Register")
    suspend fun postRegister(@Body registerRequest: RegisterRequest) : Result<AuthenticationResponse>
    @GET("/Package")
    suspend fun getPackages() : Result<List<PackageResponse>>
}