package ru.zatsoft.mynet.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.zatsoft.mynet.auth.AppAuth
import ru.zatsoft.mynet.db.Db
import ru.zatsoft.mynet.dto.Attachment
import ru.zatsoft.mynet.dto.MediaUpload
import ru.zatsoft.mynet.dto.Post
import ru.zatsoft.mynet.enumeration.AttachmentType
import ru.zatsoft.mynet.model.FeedModel
import ru.zatsoft.mynet.model.FeedState
import ru.zatsoft.mynet.model.PhotoModel
import ru.zatsoft.mynet.repository.PostRepository
import ru.zatsoft.mynet.repository.PostRepositoryImpl
import ru.zatsoft.mynet.util.SingleLiveEvent
import java.io.File
import java.io.IOException

private val noPhoto = PhotoModel()

class PostViewModel(application: Application): AndroidViewModel(application) {
    val empty = Post(
        id = 0,
        authorId = 0,
        author = "",
        authorAvatar = "404.png",
        content = "",
        published = System.currentTimeMillis().toString(),
        coords = null,
        link = null,
        mentionIds = emptySet(),
        mentionedMe = false,
        likeOwnerIds  = emptySet(),
        likedByMe = false,
        likes = 0,
        newPost = false
    )

    private val repository: PostRepository =
        PostRepositoryImpl(Db.getInstance(application).postDao())

    val data: LiveData<FeedState> =
        AppAuth.getInstance()
            .authStateFlow
            .flatMapLatest { (myId, _) ->
                repository.data
                    .map { posts ->
                        FeedModel(
                            posts.map { it.copy(ownedByMe = it.authorId == myId) },
                            posts.isEmpty()
                        )
                        FeedState.Success
                    }
            }.asLiveData(Dispatchers.Default)

    private val _dataState = MutableLiveData<FeedState>()
    val dataState: LiveData<FeedState>
        get() = _dataState

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo

    var countNewPosts: Int = 0

    val newer: LiveData<Int> = repository.data.flatMapLatest {
        val lastId = it.firstOrNull()?.id ?: 0L
        val newPosts = repository.getNewerCount(lastId)
        repository.countNew.also { countNewPosts = it }
        newPosts
    }.catch { e -> e.printStackTrace() }
        .asLiveData(Dispatchers.Default)

    val edited = MutableLiveData(empty)
    var posts: Flow<FeedModel> = repository.data.map(::FeedModel)
    var errorMessage: String = ""


    init {
        loadPosts()
        posts.asLiveData().value?.posts?.map {
            it.copy(newPost = false)
        }
    }

    fun loadPosts() {
        viewModelScope.launch {
            _dataState.value = FeedState.Loading
            try {
                repository.getAll()
                _dataState.value = FeedState.Success
            } catch (e: Exception) {
                myError(e)
                _dataState.value = FeedState.Error
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            edited.value?.let {
                try {
                    when (_photo.value) {
                        noPhoto -> repository.save(it)
                        else -> _photo.value?.file?.let { file ->
//     is uploaded yet?
                            if(!file.toString().startsWith("https:",true)){
                                repository.saveWithAttachment(it, MediaUpload(file))}
                            else
                                repository.save(it)
                        }
                    }
                    _dataState.value = FeedState.Success
                } catch (e: Exception) {
                    _dataState.value = FeedState.Error
                }
                _postCreated.value = Unit
            }
            edited.value = empty
        }
    }

    fun edit(post: Post) {
        edited.value = post
        post.attachment?.let {
            changePhoto(Uri.parse(post.attachment.url), File(post.attachment.url))
        }
    }

    fun changePost(post: Post) {
        val text = post.content.trim()
        var newAttachment: Attachment? = null
        if (photo.value != noPhoto) {
            newAttachment = Attachment(photo.value?.uri.toString(), AttachmentType.IMAGE)
        }
        if (edited.value?.content == text
            && edited.value?.attachment == post.attachment
        ) {
            return
        }
        edited.value = edited.value?.copy(content = text, attachment = newAttachment)

    }

    fun likeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.likeById(id)
                _dataState.value = FeedState.Success
            } catch (e: Exception) {
                myError(e)
                _dataState.value = FeedState.Error
            }
        }
    }


    fun removeById(id: Long) {
        val old = posts
        try {
            posts.asLiveData(Dispatchers.Default).value?.posts?.filter {
                it.id != id
            }
            viewModelScope.launch {
                try {
                    repository.removeById(id)
                } catch (e: Exception) {
                    myError(e)
                    _dataState.value = FeedState.Error
                }
            }
        } catch (e: IOException) {
            posts = old
        }
    }


    fun showNews() {
        viewModelScope.launch {
            try {
                repository.showNews()
            } catch (e: Exception) {
                myError(e)
                _dataState.value = FeedState.Error
            }
        }
    }

    fun clearCountNews() {
        countNewPosts = 0
        repository.countNew = 0
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }


    private fun myError(e: Exception) {
        e.message?.let { it ->
            _dataState.postValue(FeedState.Error)
            errorMessage = when (it) {
                "500" -> "Ошибка сервера"
                "404", "HTTP 404 " -> "Страница/пост не найдены"
                else -> "Ошибка соединения"
            }
        }
    }
}


