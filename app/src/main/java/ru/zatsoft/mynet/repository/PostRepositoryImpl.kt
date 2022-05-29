package ru.zatsoft.mynet.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.zatsoft.mynet.api.PostsApi
import ru.zatsoft.mynet.dao.PostDao
import ru.zatsoft.mynet.dto.Attachment
import ru.zatsoft.mynet.dto.Media
import ru.zatsoft.mynet.dto.MediaUpload
import ru.zatsoft.mynet.dto.Post
import ru.zatsoft.mynet.entity.PostEntity
import ru.zatsoft.mynet.enumeration.AttachmentType
import ru.zatsoft.mynet.model.ApiError
import ru.zatsoft.mynet.model.NetworkError
import ru.zatsoft.mynet.model.UnknownError
import java.io.IOException


class PostRepositoryImpl(private val dao: PostDao):PostRepository {

    override var countNew: Int = 0

    override val data = dao.getAll().map { it.map(PostEntity::toDto) }

    override suspend fun getAll() {
        val all = PostsApi.service.getAll()
        dao.removeAll()
        dao.insert(all.map { val value = it.copy(newPost =  false )
            value}
            .map(PostEntity.Companion::fromDto))
    }

    override fun getNewerCount(id: Long): Flow<Int> = flow{
        while(true) {
            try{
                val newer = PostsApi.service.getNewer(id).map(PostEntity.Companion::fromDto)
                dao.insert(newer.map { val value = it.copy(newPost = true)
                    value })
                countNew = countNew + newer.size
                emit(newer.size)
                delay(30_000L)
            }catch(e: IOException){}
        }
    }

    override suspend fun save(post: Post) {
        PostsApi.service.save(post)
    }

    override suspend fun likeById(id: Long) {
        PostsApi.service.likeById(id)
        dao.likeById(id)
    }

    override suspend fun removeById(id: Long) {
        PostsApi.service.removeById(id)
        dao.removeById(id)
    }

    override suspend fun showNews() {
        dao.showNews()
    }

    override suspend fun saveWithAttachment(post: Post, upload: MediaUpload) {
        try {
            val media = upload(upload)
            val postWithAttachment = post.copy(attachment = Attachment(media.id, AttachmentType.IMAGE))
            save(postWithAttachment)
        } catch (e: ApiError) {
            throw e
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw  UnknownError
        }
    }

    override suspend fun upload(upload: MediaUpload): Media {
        try {
            val media = MultipartBody.Part.createFormData(
                "file", upload.file.name, upload.file.asRequestBody())

            val response = PostsApi.service.upload(media)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }
}