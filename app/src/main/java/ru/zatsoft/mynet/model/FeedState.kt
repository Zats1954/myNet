package ru.zatsoft.mynet.model

sealed class FeedState {
    object Loading : FeedState()
    object Error : FeedState()
    object Refreshing : FeedState()
    object Success : FeedState()
}
