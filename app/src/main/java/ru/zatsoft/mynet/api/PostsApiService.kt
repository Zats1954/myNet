package ru.zatsoft.mynet.api

import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import ru.zatsoft.mynet.BuildConfig
import ru.zatsoft.mynet.BuildConfig.BASE_URL
import ru.zatsoft.mynet.auth.AppAuth
import ru.zatsoft.mynet.dto.Media
import ru.zatsoft.mynet.dto.Post
import java.util.concurrent.TimeUnit

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

interface PostsApiService {

    @GET("posts")
    suspend fun getAll():  List<Post>

    @GET("posts/{id}/newer")
    suspend fun getNewer(@Path("id") id: Long):List<Post>

    @POST("posts")
    suspend fun save(@Body post: Post): Post

    @DELETE("posts/{id}")
    suspend fun removeById(@Path("id") id: Long)

    @POST("posts/{id}/likes")
    suspend fun likeById(@Path("id") id: Long): Post

    @Multipart
    @POST("media")
    suspend fun upload(@Part media: MultipartBody.Part): Response<Media>

}

object PostsApi {
    val service: PostsApiService by lazy( retrofit::create )
}