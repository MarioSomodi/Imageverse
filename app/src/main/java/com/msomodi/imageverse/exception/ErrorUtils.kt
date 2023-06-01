package com.msomodi.imageverse.exception

import com.google.gson.Gson
import com.msomodi.imageverse.model.exception.ApiException
import retrofit2.HttpException

class ErrorUtils {
    fun parseError(httpException: HttpException): ApiException {
        val gson = Gson()
        val errorBodyJsonString = httpException.response()?.errorBody()!!.string()
        if(errorBodyJsonString.isBlank()){
            var message : String = httpException.localizedMessage
            when(httpException.code()){
                401 -> message = "You are not authenticated to access this resource"
            }
            return ApiException(httpException.code().toString(), message, "Failure")
        }
        return gson.fromJson(errorBodyJsonString, ApiException::class.java)
    }
}