package com.msomodi.imageverse.viewmodel.userProfile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomodi.imageverse.exception.ErrorUtils
import com.msomodi.imageverse.model.auth.request.UserPasswordUpdateRequest
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.model.exception.ApiException
import com.msomodi.imageverse.repository.AuthenticationRepository
import com.msomodi.imageverse.repository.UserRepository
import com.msomodi.imageverse.util.isValidPassword
import com.msomodi.imageverse.view.main.userProfile.ChangePasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val _userRepository : UserRepository,
    private val _authenticationRepository : AuthenticationRepository
) : ViewModel() {
    private var _changePasswordState = mutableStateOf(
        ChangePasswordState()
    )
    val changePasswordState: State<ChangePasswordState>
        get() = _changePasswordState

    private val _changePasswordRequestState = MutableStateFlow<RequestState>(RequestState.START)

    val changePasswordRequestState : Flow<RequestState>
        get() = _changePasswordRequestState

    fun onNewPasswordChanged(password: String){
        _changePasswordState.value = _changePasswordState.value.copy(
            newPassword = password,
            isNewPasswordValid = password.isValidPassword()
        )
    }

    fun onCurrentPasswordChanged(password: String){
        _changePasswordState.value = _changePasswordState.value.copy(
            currentPassword = password,
        )
    }

    private val _errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("CHANGE_PASSWORD_VIEW_MODEL", e.toString(), e)
    }

    fun updateUserPassword(id : String){
        viewModelScope.launch (_errorHandler){
            _changePasswordRequestState.emit(RequestState.LOADING)
            _userRepository.putUserPassword(
                UserPasswordUpdateRequest(
                    id,
                    changePasswordState.value.currentPassword,
                    changePasswordState.value.newPassword,
                )
            ).onSuccess {
                _changePasswordRequestState.emit(RequestState.SUCCESS)
                _changePasswordState = mutableStateOf(
                    ChangePasswordState()
                )
                _authenticationRepository.removeAuthenticatedUser()
            }.onFailure {
                if(it is HttpException){
                    val error : ApiException = ErrorUtils().parseError(it)
                    _changePasswordRequestState.emit(RequestState.FAILURE(error.title))
                }
                else
                    _changePasswordRequestState.emit(RequestState.FAILURE(it.localizedMessage))
            }
        }
    }
}