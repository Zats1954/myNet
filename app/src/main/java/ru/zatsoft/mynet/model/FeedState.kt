package ru.zatsoft.mynet.model

import ru.zatsoft.mynet.dto.Post

sealed class FeedState {
    object Loading : FeedState()
    object Error : FeedState()
    object Refreshing : FeedState()
    object Success : FeedState()
}

data class FeedModel(
    val posts: List<Post> = emptyList(),
    val empty: Boolean = false,
)