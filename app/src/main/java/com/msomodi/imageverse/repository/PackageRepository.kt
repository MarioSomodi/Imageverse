package com.msomodi.imageverse.repository

import com.msomodi.imageverse.api.ImageverseApi
import com.msomodi.imageverse.model.auth.response.PackageResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PackageRepository @Inject constructor(
    private val _imageverseApi : ImageverseApi
) {
    suspend fun getPackages() : Result<List<PackageResponse>> {
        val result =  withContext(Dispatchers.IO){
            _imageverseApi.getPackages()
        }
        return result;
    }
}