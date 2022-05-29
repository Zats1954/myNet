package ru.zatsoft.mynet.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.zatsoft.mynet.dto.Attachment
import ru.zatsoft.mynet.dto.Post
import ru.zatsoft.mynet.enumeration.AttachmentType

@Entity
data class PostEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val content: String,
    val published: String,
    val coords:String?,
    val link: String?,
//    @ElementCollection
//    val mentionIds:MutableSet<Long>,
    val mentionedMe:Boolean,
//    val likeOwnerIds:MutableSet<Long>,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val ownedByMe: Boolean = false,
    var newPost: Boolean = true,
    @Embedded
    var attachment: AttachmentEmbeddable?,
) {

    fun toDto() = Post(id, authorId, author, authorAvatar, content, published,coords, link,
        mentionedMe = mentionedMe, likedByMe = likedByMe, likes = likes, newPost = newPost,
        ownedByMe = ownedByMe, attachment = attachment?.toDto())
    companion object{
        fun fromDto(dto: Post) = PostEntity(dto.id, dto.authorId, dto.author,
            dto.authorAvatar, dto.content, dto.published,
            dto.coords, dto.link, dto.mentionedMe,
            dto.likedByMe, dto.likes, dto.newPost, dto.ownedByMe, AttachmentEmbeddable.fromDto(dto.attachment))

    }
}

class AttachmentEmbeddable (
    var url: String,
    var type: AttachmentType
){
    fun toDto() = Attachment(url,type)
    companion object{
        fun fromDto(dto: Attachment?) = dto?.let{
            AttachmentEmbeddable(it.url,it.type)
        }
    }

}
fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)