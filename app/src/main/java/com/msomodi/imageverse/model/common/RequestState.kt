package com.msomodi.imageverse.model.common

sealed class RequestState {
    object START : RequestState()
    object LOADING : RequestState()
    object SUCCESS : RequestState()
    data class FAILURE(val message: String) : RequestState()
}
