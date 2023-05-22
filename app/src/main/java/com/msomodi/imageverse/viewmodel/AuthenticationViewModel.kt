package com.msomodi.imageverse.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomodi.imageverse.repository.AuthenticationRepository
import com.msomodi.imageverse.util.isValidPassword
import com.msomodi.imageverse.view.auth.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val _authenticationRepository: AuthenticationRepository
) : ViewModel() {
    private val _authenticationState = mutableStateOf(
        AuthenticationState()
    )

    val authenticationState: State<AuthenticationState>
        get() = _authenticationState

    fun onEmailChanged(email: String){
        _authenticationState.value = _authenticationState.value.copy(
            email = email,
            isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        )
    }

    fun onPasswordChanged(password: String){
        _authenticationState.value = _authenticationState.value.copy(
            password = password,
            isPasswordValid = password.isValidPassword()
        )
    }

    private val errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("AUTHENTICATION_VIEWMODEL", e.toString(), e)
    }
    fun logIn(onSuccess: () -> Unit, onFail: () -> Unit, onSuccessHigherPrivileges: () -> Unit){
        viewModelScope.launch (errorHandler){
                _authenticationRepository.postLogin(
                    authenticationState.value.email,
                    authenticationState.value.password,
                    onSuccess,
                    onFail,
                    onSuccessHigherPrivileges)
        }
    }
}