package ru.zatsoft.mynet.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.zatsoft.mynet.dao.PostDao
import ru.zatsoft.mynet.entity.PostEntity


@Database(entities = [PostEntity::class], version = 1)
abstract class Db : RoomDatabase() {
    abstract fun postDao(): PostDao

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