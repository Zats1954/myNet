package ru.zatsoft.mynet.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.Response
import ru.zatsoft.mynet.dto.*

interface SignRepository {
    suspend fun autorization(login: String, pass: String):  Token
    suspend fun registration(login: String, pass: String, name:String):  Token
}

interface PostRepository {
    var countNew: Int
    val data: Flow<List<Post>>
    suspend fun getAll()
    fun getNewerCount(id:Long): Flow<Int>
    suspend fun save(post:Post)
    suspend fun likeById(id:Long)
    suspend fun removeById(id: Long)
    suspend fun showNews()
    suspend fun saveWithAttachment(post: Post, upload: MediaUpload)
    suspend fun upload(upload: MediaUpload): Media
}