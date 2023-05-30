package com.msomodi.imageverse.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.msomodi.imageverse.repository.AuthenticationRepository
import com.msomodi.imageverse.util.isValidPassword
import com.msomodi.imageverse.view.auth.RegisterState
import com.msomodi.imageverse.view.common.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val _authenticationRepository : AuthenticationRepository
) : ViewModel() {
    private val _registerState = mutableStateOf(
        RegisterState()
    )
    val registerState: State<RegisterState>
        get() = _registerState

    val loginRequestState = MutableStateFlow<RequestState>(RequestState.START)

    private val _errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("REGISTER_VIEW_MODEL", e.toString(), e)
    }

    fun onEmailChanged(email: String){
        _registerState.value = _registerState.value.copy(
            email = email,
            isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        )
    }

    fun onPasswordChanged(password: String){
        _registerState.value = _registerState.value.copy(
            password = password,
            isPasswordValid = password.isValidPassword()
        )
    }

    fun onNameChanged(name: String){
        _registerState.value = _registerState.value.copy(
            name = name,
            isNameValid = name.isNotBlank()
        )
    }

    fun onSurnameChanged(surname: String){
        _registerState.value = _registerState.value.copy(
            surname = surname,
            isSurnameValid = surname.isNotBlank()
        )
    }

    fun onUsernameChanged(username: String){
        _registerState.value = _registerState.value.copy(
            username = username,
            isUsernameValid = username.isNotBlank()
        )
    }

    fun onPackageIdChange(packageId: String){
        _registerState.value = _registerState.value.copy(
            packageId = packageId,
            isPackageIdValid = packageId.isNotBlank()
        )
    }
}