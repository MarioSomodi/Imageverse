package com.msomodi.imageverse.api

import com.msomodi.imageverse.model.auth.request.LoginRequest
import com.msomodi.imageverse.model.auth.request.RefreshRequest
import com.msomodi.imageverse.model.auth.request.RegisterRequest
import com.msomodi.imageverse.model.auth.request.UserEmailUpdateRequest
import com.msomodi.imageverse.model.auth.request.UserInfoUpdateRequest
import com.msomodi.imageverse.model.auth.request.UserIsAdminUpdateRequest
import com.msomodi.imageverse.model.auth.request.UserPackageUpdateRequest
import com.msomodi.imageverse.model.auth.request.UserPasswordUpdateRequest
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.model.auth.response.BoolResponse
import com.msomodi.imageverse.model.auth.response.RefreshResponse
import com.msomodi.imageverse.model.auth.response.UserLimitResponse
import com.msomodi.imageverse.model.auth.response.UserResponse
import com.msomodi.imageverse.model.packages.response.PackageResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @PUT("/User/Info")
    suspend fun putUserInfo(@Body userInfoUpdateRequest: UserInfoUpdateRequest) : Result<UserResponse>

    @PUT("/User/Email")
    suspend fun putUserEmail(@Body userEmailUpdateRequest: UserEmailUpdateRequest) : Result<BoolResponse>

    @PUT("/User/Password")
    suspend fun putUserPassword(@Body userPasswordUpdateRequest: UserPasswordUpdateRequest) : Result<BoolResponse>

    @PUT("/User/Package")
    suspend fun putUserPackage(@Body userPackageUpdateRequest: UserPackageUpdateRequest) : Result<BoolResponse>

    //TODO figure out how to send image in request body key (string binary)
    @PUT("/User/Image/{id}")
    suspend fun putUserImage(@Path("id") id : String) : Result<AuthenticationResponse>

    @PUT("/User/Admin")
    suspend fun putUserIsAdmin(@Body userIsAdminUpdateRequest: UserIsAdminUpdateRequest) : Result<BoolResponse>
    @GET("/UserLimit/{date}/{id}")
    suspend fun getUserLimitOnDate(@Path("date") date : String, @Path("id") id : String) : Result<UserLimitResponse>
}