package com.msomodi.imageverse.api

import com.msomodi.imageverse.model.auth.request.LoginRequest
import com.msomodi.imageverse.model.auth.request.RefreshRequest
import com.msomodi.imageverse.model.auth.request.RegisterRequest
import com.msomodi.imageverse.model.user.request.UserEmailUpdateRequest
import com.msomodi.imageverse.model.user.request.UserInfoUpdateRequest
import com.msomodi.imageverse.model.user.request.UserIsAdminUpdateRequest
import com.msomodi.imageverse.model.user.request.UserPackageUpdateRequest
import com.msomodi.imageverse.model.user.request.UserPasswordUpdateRequest
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.model.common.BoolResponse
import com.msomodi.imageverse.model.post.response.PostResponse
import com.msomodi.imageverse.model.auth.response.RefreshResponse
import com.msomodi.imageverse.model.hashtag.response.HashtagResponse
import com.msomodi.imageverse.model.userLimit.response.UserLimitResponse
import com.msomodi.imageverse.model.user.response.UserResponse
import com.msomodi.imageverse.model.packages.response.PackageResponse
import com.msomodi.imageverse.model.post.request.CreatePostRequest
import com.msomodi.imageverse.model.post.request.EditPostRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("/Hashtag")
    suspend fun getHashtags() : Result<List<HashtagResponse>>

    @GET("/Post")
    suspend fun getPosts(
        @Query("page") page: Int = 1
    ) : List<PostResponse>

    @GET("/Post/{id}")
    suspend fun getPost(@Path("id") id : String) : Result<PostResponse>

    @DELETE("/Post/{id}")
    suspend fun deletePost(@Path("id") id : String) : Result<BoolResponse>

    @PUT("/Post")
    suspend fun putPost(@Body editPostRequest: EditPostRequest) : Result<PostResponse>

    @POST("/Post")
    suspend fun createPost(@Body createPostRequest: CreatePostRequest) : Result<PostResponse>

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