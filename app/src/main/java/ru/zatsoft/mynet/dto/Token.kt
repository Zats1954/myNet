package ru.zatsoft.mynet.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Token(
    val id:Long,
    val token: String
): Parcelable


