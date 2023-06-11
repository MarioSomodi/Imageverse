package com.msomodi.imageverse.api

import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.msomodi.imageverse.db.ImageverseDatabase
import com.msomodi.imageverse.exception.ResultCallAdapterFactory
import com.msomodi.imageverse.model.auth.request.RefreshRequest
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import java.net.HttpURLConnection
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class AuthInterceptor @Inject constructor(
private val _imageverseDatabase: ImageverseDatabase,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var authResult : AuthenticationResponse? = getAuthenticatedUser()
        var token : String? = authResult?.token
        val refreshToken = authResult?.user?.refreshToken
        val request = chain.request()
        if (!token.isNullOrBlank() && !refreshToken.isNullOrBlank() && authResult?.authenticatedUserId != null) {
            val currentMoment: Instant = Clock.System.now()
            val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)
            //If package was changed and today is the day it is to be valid but user has still not logged out log him out for package change to take effect
            if(authResult.user?.activePackageId == authResult.user?.previousPackageId &&
                datetimeInUtc.date >= LocalDateTime.parse(authResult.user?.packageValidFrom?.dropLast(1)+"0").date &&
                authResult.user?.userStatistics?.totalTimesUserRequestedPackageChange!! > 0
            ){
                runBlocking(Dispatchers.IO) {
                    _imageverseDatabase.authenticatedUsersDao().deleteAuthenticatedUser()
                }
            }
            val response = chain.proceed(newRequestWithAccessToken(token, request))
            if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                authResult = getAuthenticatedUser()
                val newToken = authResult?.token
                if (newToken != token) {
                    return chain.proceed(newRequestWithAccessToken(newToken, request))
                } else {
                    token = refreshToken(refreshToken, token, authResult?.authenticatedUserId!!)
                    if (token.isNullOrBlank()) {
                        runBlocking(Dispatchers.IO) {
                            _imageverseDatabase.authenticatedUsersDao().deleteAuthenticatedUser()
                        }
                        return response
                    }
                    return chain.proceed(newRequestWithAccessToken(token, request))
                }
            }else{
                return response
            }

        }
        return chain.proceed(request)
    }

    private fun getAuthenticatedUser() : AuthenticationResponse?{
        val authenticatedUser: AuthenticationResponse?
        runBlocking(Dispatchers.IO) {
            val authResult = _imageverseDatabase.authenticatedUsersDao().getAuthenticatedUser().first()
            authenticatedUser = authResult
        }
        return authenticatedUser
    }

    private fun newRequestWithAccessToken(token: String?, request: Request): Request =
        request.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

    private fun refreshToken(refreshToken : String, token : String, authenticatedUserId : Int): String? {
        var newToken : String? = null
        runBlocking(Dispatchers.IO) {
            val trustAllCerts = setupTrustManager();

            val sslSocketFactory = getSSLSocketFactory(trustAllCerts)

            val client = OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager).hostnameVerifier{ _, _ -> true }
                .build()

            val json = Json {
                ignoreUnknownKeys = true
            }

            val api = Retrofit.Builder()
                .baseUrl("https://10.0.2.2:7214/Authentication/")
                .client(client)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .addCallAdapterFactory(ResultCallAdapterFactory())
                .build()
                .create(ImageverseApi::class.java)

            api.postRefresh(RefreshRequest(token, refreshToken)).onSuccess {
                newToken = it.accessToken
                _imageverseDatabase.authenticatedUsersDao().updateToken(authenticatedUserId = authenticatedUserId, newToken!!)
            }.onFailure {
                Log.e("REFERSH ERROR " + it.message, it.toString())
            }
        }
        return newToken
    }
}

private fun setupTrustManager() : Array<TrustManager>{
    return  arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
        }
        override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
    })
}

private fun getSSLSocketFactory(trustAllCerts : Array<TrustManager>) : SSLSocketFactory {
    val sslContext = SSLContext.getInstance("SSL")
    sslContext.init(null, trustAllCerts, SecureRandom())

    return sslContext.socketFactory
}