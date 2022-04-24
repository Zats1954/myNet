package ru.zatsoft.mynet.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.zatsoft.mynet.dto.User


@Entity
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val login: String,
    val pass: String,
    val name: String,
    val avatar: String? =null,
    val authorities: String,
) {
    fun toDto() = User(id,login, pass, name, avatar, authorities)

    companion object {
        fun fromDto(dto: User) =
            UserEntity(dto.id, dto.login , dto.pass, dto.name, dto.avatar, dto.authorities)

    }
}

