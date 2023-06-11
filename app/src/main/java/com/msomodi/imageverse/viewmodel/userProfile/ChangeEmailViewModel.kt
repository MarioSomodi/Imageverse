package com.msomodi.imageverse.viewmodel.userProfile

import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomodi.imageverse.exception.ErrorUtils
import com.msomodi.imageverse.model.auth.request.UserEmailUpdateRequest
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.model.exception.ApiException
import com.msomodi.imageverse.repository.AuthenticationRepository
import com.msomodi.imageverse.repository.UserRepository
import com.msomodi.imageverse.view.main.userProfile.ChangeEmailState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val _userRepository : UserRepository,
    private val _authenticationRepository : AuthenticationRepository
) : ViewModel() {
    private var _changeEmailState = mutableStateOf(
        ChangeEmailState()
    )
    val changeEmailState: State<ChangeEmailState>
        get() = _changeEmailState

    private val _changeEmailRequestState = MutableStateFlow<RequestState>(RequestState.START)

    val changeEmailRequestState : Flow<RequestState>
        get() = _changeEmailRequestState

    fun onEmailChanged(email: String){
        _changeEmailState.value = _changeEmailState.value.copy(
            email = email,
            isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        )
    }

    private val _errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("CHANGE_EMAIL_VIEW_MODEL", e.toString(), e)
    }

    fun updateUserEmail(id : String, authenticationType : Int){
        viewModelScope.launch (_errorHandler){
            _changeEmailRequestState.emit(RequestState.LOADING)
            _userRepository.putUserEmail(
                UserEmailUpdateRequest(
                    id,
                    changeEmailState.value.email,
                    authenticationType
                )
            ).onSuccess {
                _changeEmailRequestState.emit(RequestState.SUCCESS)
                _changeEmailState = mutableStateOf(
                    ChangeEmailState()
                )
                _authenticationRepository.removeAuthenticatedUser()
            }.onFailure {
                if(it is HttpException){
                    val error : ApiException = ErrorUtils().parseError(it)
                    _changeEmailRequestState.emit(RequestState.FAILURE(error.title))
                }
                else
                    _changeEmailRequestState.emit(RequestState.FAILURE(it.localizedMessage))
            }
        }
    }

}