package ru.zatsoft.mynet.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.RawValue
import ru.zatsoft.mynet.enumeration.AttachmentType

@Parcelize
data class Post (
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
    val coords:String?,
    val link: String?,
    val mentionIds:Set<Long> = emptySet(),
    val mentionedMe:Boolean,
    val likeOwnerIds:Set<Long> = emptySet(),
    val likedByMe: Boolean,
    val likes: Int = 0,
    val newPost: Boolean = true,
    val ownedByMe: Boolean = false,
    val attachment: Attachment? =null
): Parcelable

@Parcelize
data class Attachment(
    val url: String,
    val type: AttachmentType,
): Parcelable