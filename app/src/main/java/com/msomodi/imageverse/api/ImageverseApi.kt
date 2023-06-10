package com.msomodi.imageverse.api

import com.msomodi.imageverse.model.auth.request.LoginRequest
import com.msomodi.imageverse.model.auth.request.RefreshRequest
import com.msomodi.imageverse.model.auth.request.RegisterRequest
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.model.auth.response.BoolResponse
import com.msomodi.imageverse.model.auth.response.RefreshResponse
import com.msomodi.imageverse.model.packages.response.PackageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ImageverseApi {
    @POST("/Authentication/Login")
    suspend fun postLogin(@Body loginRequest : LoginRequest) : Result<AuthenticationResponse>

    @POST("/Authentication/Register")
    suspend fun postRegister(@Body registerRequest: RegisterRequest) : Result<AuthenticationResponse>

    @POST("/Authentication/Refresh")
    suspend fun postRefresh(@Body refreshRequest: RefreshRequest) : Result<RefreshResponse>

    @GET("/Authentication/UserExists/{authenticationProviderId}")
    suspend fun getUserExists(@Path("authenticationProviderId") authenticationProviderId : String) : Result<BoolResponse>

    @GET("/Package")
    suspend fun getPackages() : Result<List<PackageResponse>>

    @GET("/Package/{id}")
    suspend fun getPackage(@Path("id") id : String) : Result<PackageResponse>
}