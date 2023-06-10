package com.msomodi.imageverse.viewmodel.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val _authenticationRepository: AuthenticationRepository,
) : ViewModel() {
    private val _authenticatedUser : MutableLiveData<AuthenticationResponse?> = MutableLiveData()
    val authenticatedUser: LiveData<AuthenticationResponse?>
        get() = _authenticatedUser

    private val _errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("AUTHENTICATION_VIEWMODEL", e.toString(), e)
    }

    fun getAuthenticatedUser() = viewModelScope.launch(_errorHandler){
        _authenticationRepository.getAuthenticatedUser().collect{
            if(it == null){
                _authenticatedUser.postValue(AuthenticationResponse(-1, null, ""))
            }else{
                _authenticatedUser.postValue(it)
            }
        }
    }

    fun removeAuthenticatedUser() = viewModelScope.launch(_errorHandler){
        _authenticationRepository.removeAuthenticatedUser()
    }
}