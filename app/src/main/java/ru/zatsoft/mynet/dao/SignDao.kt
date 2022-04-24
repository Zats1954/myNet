package ru.zatsoft.mynet.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

import ru.zatsoft.mynet.entity.UserEntity


@Dao
interface SignDao {
    @Query("SELECT * FROM UserEntity WHERE id= :id")
    fun getUser(id:Int): LiveData<List<UserEntity>>

    @Insert
    fun regUser(user:UserEntity)
}