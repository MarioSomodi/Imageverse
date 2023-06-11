package com.msomodi.imageverse.repository

import com.msomodi.imageverse.api.ImageverseApi
import com.msomodi.imageverse.model.auth.request.UserEmailUpdateRequest
import com.msomodi.imageverse.model.auth.request.UserInfoUpdateRequest
import com.msomodi.imageverse.model.auth.request.UserIsAdminUpdateRequest
import com.msomodi.imageverse.model.auth.request.UserPackageUpdateRequest
import com.msomodi.imageverse.model.auth.request.UserPasswordUpdateRequest
import com.msomodi.imageverse.model.auth.response.BoolResponse
import com.msomodi.imageverse.model.auth.response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val _imageverseApi : ImageverseApi
) {
    suspend fun putUserInfo(userInfoUpdateRequest: UserInfoUpdateRequest) : Result<UserResponse> {
        return withContext(Dispatchers.IO){
            _imageverseApi.putUserInfo(userInfoUpdateRequest)
        }
    }

    suspend fun putUserEmail(userEmailUpdateRequest: UserEmailUpdateRequest) : Result<BoolResponse>  {
        return withContext(Dispatchers.IO){
            _imageverseApi.putUserEmail(userEmailUpdateRequest)
        }
    }

    suspend fun putUserPassword(userPasswordUpdateRequest: UserPasswordUpdateRequest) : Result<BoolResponse>  {
        return withContext(Dispatchers.IO){
            _imageverseApi.putUserPassword(userPasswordUpdateRequest)
        }
    }

    suspend fun putUserPackage(userPackageUpdateRequest: UserPackageUpdateRequest) : Result<BoolResponse>  {
        return withContext(Dispatchers.IO){
            _imageverseApi.putUserPackage(userPackageUpdateRequest)
        }
    }

    suspend fun putUserIsAdmin(userIsAdminUpdateRequest: UserIsAdminUpdateRequest) : Result<BoolResponse>  {
        return withContext(Dispatchers.IO){
            _imageverseApi.putUserIsAdmin(userIsAdminUpdateRequest)
        }
    }
}