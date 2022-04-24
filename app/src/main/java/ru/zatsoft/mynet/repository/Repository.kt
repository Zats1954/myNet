package ru.zatsoft.mynet.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.Response
import ru.zatsoft.mynet.dto.Token
import ru.zatsoft.mynet.dto.User

interface SignRepository {
    val data: Flow<List<User>>
    suspend fun getAll()
    suspend fun autorization(login: String, pass: String):  Token
    suspend fun registration(login: String, pass: String, name:String):  Token
//    fun getNewerCount(id:Long): Flow<Int>
//    suspend fun save(post:User)
//    suspend fun likeById(id:Long)
//    suspend fun removeById(id: Long)
//    suspend fun showNews()
//    suspend fun saveWithAttachment(post: User, upload: MediaUpload)
//    suspend fun upload(upload: MediaUpload): Media
//    suspend fun autorization(login: String, pass: String): Token
//    suspend fun makeUser(login: String, pass: String, name: String): Token
}