package com.msomodi.imageverse.view.common

sealed class RequestState {
    object START : RequestState()
    object LOADING : RequestState()
    object SUCCESS : RequestState()
    data class FAILURE(val message: String) : RequestState()
}
