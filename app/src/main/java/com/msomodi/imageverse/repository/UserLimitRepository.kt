package com.msomodi.imageverse.repository

import com.msomodi.imageverse.api.ImageverseApi
import com.msomodi.imageverse.model.userLimit.response.UserLimitResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserLimitRepository @Inject constructor(
    private val _imageverseApi : ImageverseApi
) {
    suspend fun getUserLimitOnDate(date : String, id : String) : Result<UserLimitResponse> {
        val result =  withContext(Dispatchers.IO){
            _imageverseApi.getUserLimitOnDate(date, id)
        }
        return result;
    }
}