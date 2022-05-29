package ru.zatsoft.mynet.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.zatsoft.mynet.api.PostsApi
import ru.zatsoft.mynet.api.SignApi
import ru.zatsoft.mynet.dao.PostDao
import ru.zatsoft.mynet.dto.Token
import ru.zatsoft.mynet.dto.User
import ru.zatsoft.mynet.entity.PostEntity
import ru.zatsoft.mynet.model.ApiError
import ru.zatsoft.mynet.model.NetworkError
import ru.zatsoft.mynet.model.UnknownError
import java.io.IOException

class SignRepositoryImpl() : SignRepository {

    override suspend fun autorization(login: String, pass: String): Token {
       try {
           println("login $login pass $pass")
           val response = SignApi.service.authentication(login,pass)
           println("response ${response}")
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
