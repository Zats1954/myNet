package ru.zatsoft.mynet.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.zatsoft.mynet.dao.SignDao
import ru.zatsoft.mynet.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class Db : RoomDatabase() {
    abstract fun signDao(): SignDao

    companion object {
        @Volatile
        private var instance: Db? = null

        fun getInstance(context: Context): Db {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, Db::class.java,
                "app.db")
                .allowMainThreadQueries()
                .build()
    }
}