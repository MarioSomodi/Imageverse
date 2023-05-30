package com.msomodi.imageverse.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomodi.imageverse.exception.ErrorUtils
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
class LoginViewModel @Inject constructor(
    private val _authenticationRepository: AuthenticationRepository,
    ) : ViewModel() {
    private val _loginState = mutableStateOf(
        LoginState()
    )
    val loginState: State<LoginState>
        get() = _loginState

    val loginRequestState = MutableStateFlow<RequestState>(RequestState.START)

    private val _errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("LOGIN_VIEW_MODEL", e.toString(), e)
    }

    fun onEmailChanged(email: String){
        _loginState.value = _loginState.value.copy(
            email = email,
            isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        )
    }

    fun onPasswordChanged(password: String){
        _loginState.value = _loginState.value.copy(
            password = password,
        )
    }

    fun logIn(onSuccess: () -> Unit, onSuccessHigherPrivileges: () -> Unit){
        viewModelScope.launch (_errorHandler){
            loginRequestState.emit(RequestState.LOADING)
            _authenticationRepository.postLogin(
                loginState.value.email,
                loginState.value.password).onSuccess {
                loginRequestState.emit(RequestState.SUCCESS)
                if(it.user!!.isAdmin){
                    onSuccessHigherPrivileges()
                }else{
                    onSuccess()
                }
            }.onFailure {
                if(it is HttpException){
                    val error : ApiException = ErrorUtils().parseError(it)
                    loginRequestState.emit(RequestState.FAILURE(error.title))
                }
                else
                    loginRequestState.emit(RequestState.FAILURE(it.localizedMessage))
            }
        }
    }
}