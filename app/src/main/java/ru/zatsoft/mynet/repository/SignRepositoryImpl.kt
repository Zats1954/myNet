package ru.zatsoft.mynet.repository

import kotlinx.coroutines.flow.Flow
import ru.zatsoft.mynet.api.SignApi
import ru.zatsoft.mynet.dto.Token
import ru.zatsoft.mynet.dto.User
import ru.zatsoft.mynet.model.ApiError
import ru.zatsoft.mynet.model.NetworkError
import ru.zatsoft.mynet.model.UnknownError
import java.io.IOException

class SignRepositoryImpl() : SignRepository {

    override val data: Flow<List<User>>
        get() = TODO("Not yet implemented")

    override suspend fun getAll() {
        TODO("Not yet implemented")
    }

    override suspend fun autorization(login: String, pass: String): Token {
       try {
           println("login $login pass $pass")
           val response = SignApi.service.authentication(login,pass)
           if (!response.isSuccessful) {
               throw ApiError(response.code(), response.message())
           }
           println("responseBody ${response.body()}")
           return response.body() ?: throw ApiError(response.code(), response.message())
       } catch (e: IOException) {
           throw NetworkError
       } catch (e: Exception) {
                      throw UnknownError
       }
    }

    override suspend fun registration(login: String, pass: String, name:String): Token {
        try {
            val response = SignApi.service.registration(login, pass, name)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw  UnknownError
        }
    }

}
