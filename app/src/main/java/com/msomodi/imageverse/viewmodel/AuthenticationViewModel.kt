package com.msomodi.imageverse.viewmodel

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.msomodi.imageverse.util.isValidPassword
import com.msomodi.imageverse.view.auth.AuthenticationState

class AuthenticationViewModel : ViewModel() {
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
}