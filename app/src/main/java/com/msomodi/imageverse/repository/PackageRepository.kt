package com.msomodi.imageverse.repository

import com.msomodi.imageverse.api.ImageverseApi
import com.msomodi.imageverse.model.packages.response.PackageResponse
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

    suspend fun getPackage(id : String) : Result<PackageResponse> {
        val result =  withContext(Dispatchers.IO){
            _imageverseApi.getPackage(id)
        }
        return result;
    }
}