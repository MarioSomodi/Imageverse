package com.msomodi.imageverse.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.msomodi.imageverse.api.AuthInterceptor
import com.msomodi.imageverse.api.ImageverseApi
import com.msomodi.imageverse.db.ImageverseDatabase
import com.msomodi.imageverse.exception.ResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


private const val IMAGEVERSE_API_URL = "https://10.0.2.2:7214/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton // created at - Application#onCreate, destroyed at - Application#onDestroy
    fun provideOkHttpClient(
        imageverseDatabase : ImageverseDatabase,
    ): OkHttpClient {
        val trustAllCerts = setupTrustManager();

        val sslSocketFactory = getSSLSocketFactory(trustAllCerts)

        val authInterceptor = AuthInterceptor(imageverseDatabase)

        return OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager).hostnameVerifier{ _, _ -> true }
            .build()
    }

    @Provides
    @Singleton
    @ExperimentalSerializationApi
    fun provideImageverseApi(okHttpClientAuth: OkHttpClient) : ImageverseApi {
        val json = Json {
            ignoreUnknownKeys = true
        }
        return Retrofit.Builder()
            .baseUrl(IMAGEVERSE_API_URL)
            .client(okHttpClientAuth)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .build()
            .create(ImageverseApi::class.java)
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
}