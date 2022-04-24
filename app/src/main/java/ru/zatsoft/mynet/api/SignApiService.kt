package ru.zatsoft.mynet.api

import ru.zatsoft.mynet.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import ru.zatsoft.mynet.auth.AppAuth
import ru.zatsoft.mynet.dto.Token
import ru.zatsoft.mynet.dto.User
import java.util.concurrent.TimeUnit

//private const val BASE_URL = "${BuildConfig.BASE_URL}"

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private val okhttp = OkHttpClient.Builder()
    .addInterceptor(logging)
    .addInterceptor{chain ->
        AppAuth.getInstance().authStateFlow.value.token?.let { token ->
            val newRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", token)
                .build()
            return@addInterceptor chain.proceed(newRequest)
        }
        chain.proceed(chain.request())}
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()



private val retrofit = Retrofit.Builder()
//    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl("${BuildConfig.BASE_URL}/api/users/")
    .client(okhttp)
    .build()

interface SignApiService {
    @GET()
    fun getAll(): Call<List<User>>

    @GET("/{id}")
    fun getById(@Path("id") id: Long): Call<User>

    @FormUrlEncoded
    @POST("/authentication")
    suspend fun authentication(@Path("login") login: String, @Path("pass") pass: String): Response<Token>

    @DELETE("Users/{id}/likes")
    fun dislikeById(@Path("id") id: Long): Call<User>

    @FormUrlEncoded
    @POST("/registration")
    suspend fun registration(@Path("login")login: String,  @Path("pass") pass: String, @Path("name") name: String): Response<Token>
}

object SignApi {
    val retrofitService: SignApiService by lazy {
        retrofit.create(SignApiService::class.java)
    }
}