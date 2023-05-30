package com.msomodi.imageverse.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomodi.imageverse.exception.ErrorUtils
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.model.exception.ApiException
import com.msomodi.imageverse.repository.AuthenticationRepository
import com.msomodi.imageverse.util.isValidPassword
import com.msomodi.imageverse.view.auth.LoginState
import com.msomodi.imageverse.view.common.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
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