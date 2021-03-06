package ru.zatsoft.mynet.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.create
import ru.zatsoft.mynet.BuildConfig
import ru.zatsoft.mynet.BuildConfig.BASE_URL
import ru.zatsoft.mynet.auth.AppAuth
import ru.zatsoft.mynet.dto.Token
import ru.zatsoft.mynet.dto.User
import java.util.concurrent.TimeUnit


// private const val BASE_URL = "${BuildConfig.BASE_URL}"

private val logging = HttpLoggingInterceptor().apply {
    if (BuildConfig.DEBUG) {
        level = HttpLoggingInterceptor.Level.BODY
    }
}

private var okhttp = OkHttpClient.Builder()
    .addInterceptor(logging)
    .addInterceptor{chain ->
        AppAuth.getInstance().authStateFlow.value.token?.let { token ->
            println("token $token")
            val newRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", token)
                .build()
            return@addInterceptor chain.proceed(newRequest)
        }
        println("No token")
        chain.proceed(chain.request())}
    .connectTimeout(30, TimeUnit.SECONDS)
    .build()



private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .client(okhttp)
    .build()

interface SignApiService {
    @GET("users/")
    fun getAll(): Response<List<User>>

    @GET("users//{id}")
    fun getById(@Path("id") id: Long): Response<User>

    @FormUrlEncoded
    @POST("users/authentication/")
    suspend fun authentication(@Field("login") login: String,@Field("pass") pass: String): Response<Token>

    @DELETE(" users/Users/{id}/likes")
    fun dislikeById(@Path("id") id: Long): Response<User>

    @FormUrlEncoded
    @POST("users/registration/")
    suspend fun registration(@Field("login")login: String,  @Field("pass") pass: String, @Field("name") name: String): Response<Token>
}

object SignApi {
    val service: SignApiService by lazy  (retrofit::create)
}