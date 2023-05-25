package com.msomodi.imageverse.exception

import com.google.gson.Gson
import com.msomodi.imageverse.model.exception.ApiException
import retrofit2.HttpException

class ErrorUtils {
    fun parseError(httpException: HttpException): ApiException {
        val gson = Gson()
        val errorBodyJsonString = httpException.response()?.errorBody()!!.string()
        return gson.fromJson(errorBodyJsonString, ApiException::class.java)
    }
}