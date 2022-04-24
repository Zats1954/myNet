package ru.zatsoft.mynet.dto

data class User(
    val id: Long,
    val login: String,
    val pass: String,
    val name: String,
    val avatar: String? =null,
    val authorities: String,
//    val file:  MultipartFile?
)
