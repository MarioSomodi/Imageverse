package com.msomodi.imageverse.repository

import com.msomodi.imageverse.api.ImageverseApi
import com.msomodi.imageverse.model.hashtag.response.HashtagResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HashtagRepository @Inject constructor(
    private val _imageverseApi : ImageverseApi
) {
    suspend fun getHashtags() : Result<List<HashtagResponse>> {
        val result =  withContext(Dispatchers.IO){
            _imageverseApi.getHashtags()
        }
        return result;
    }
}