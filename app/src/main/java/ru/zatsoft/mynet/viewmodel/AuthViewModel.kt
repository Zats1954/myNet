package ru.zatsoft.mynet.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.zatsoft.mynet.model.FeedState
import ru.zatsoft.mynet.auth.AppAuth
import ru.zatsoft.mynet.auth.AuthState
import ru.zatsoft.mynet.repository.SignRepositoryImpl
import ru.zatsoft.mynet.repository.SignRepository
import ru.zatsoft.mynet.util.SingleLiveEvent

class AuthViewModel(application: Application): AndroidViewModel(application) {

    val data: LiveData<AuthState> = AppAuth.getInstance()
        .authStateFlow
        .asLiveData(Dispatchers.Default)

    private val _dataState = MutableLiveData<FeedState>()
    val dataState: LiveData<FeedState>
        get() = _dataState

    private val _authCreated = SingleLiveEvent<Unit>()
    val authCreated: LiveData<Unit>
        get() = _authCreated

    private var errorMessage: String = ""

    private val repository: SignRepository =
        SignRepositoryImpl()

    val authenticated: Boolean
        get() = AppAuth.getInstance().authStateFlow.value.id != 0L

    fun signUser(login: String, pass: String) {
        viewModelScope.launch {
            _dataState.value = FeedState.Loading
            try {
                repository.autorization(login, pass).let{
                    println("Token $it")
                    AppAuth.getInstance().setAuth(it.id, it.token)}
                _dataState.value = FeedState.Success
            } catch (e: Exception) {
                myError(e)
                _dataState.value = FeedState.Error
            }
        }
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

    fun save() {
        println("saved")
    }

    fun loadPosts() {
        TODO("Not yet implemented")
    }

    fun signUp(login: String, pass: String,name: String) {
        println("login ${login}, pass ${pass} ,name ${name}")
        viewModelScope.launch {
            _dataState.value = FeedState.Loading
            try {
                repository.registration (login, pass, name).let{
                    AppAuth.getInstance().setAuth(it.id, it.token)}
                _dataState.value = FeedState.Success
            } catch (e: Exception) {
                myError(e)
                _dataState.value = FeedState.Error
            }
        }

    }



}
