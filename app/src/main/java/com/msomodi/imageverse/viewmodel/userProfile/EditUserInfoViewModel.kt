package com.msomodi.imageverse.viewmodel.userProfile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomodi.imageverse.exception.ErrorUtils
import com.msomodi.imageverse.model.user.request.UserInfoUpdateRequest
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.model.exception.ApiException
import com.msomodi.imageverse.repository.AuthenticationRepository
import com.msomodi.imageverse.repository.UserRepository
import com.msomodi.imageverse.view.main.userProfile.EditUserInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class EditUserInfoViewModel @Inject constructor(
    private val _userRepository : UserRepository,
    private val _authenticationRepository : AuthenticationRepository
) : ViewModel() {
    private var _editUserInfoState = mutableStateOf(
        EditUserInfoState()
    )
    val editUserInfoState: State<EditUserInfoState>
        get() = _editUserInfoState

    private val _editUserInfoRequestState = MutableStateFlow<RequestState>(RequestState.START)

    val editUserInfoRequestState : Flow<RequestState>
        get() = _editUserInfoRequestState

    fun setInitialValuesInState(username: String, name: String, surname: String){
        _editUserInfoState.value = _editUserInfoState.value.copy(
            username = username,
            name = name,
            surname = surname,
            isUsernameValid = username.isNotBlank(),
            isSurnameValid = surname.isNotBlank(),
            isNameValid = name.isNotBlank()
        )
    }

    fun onUsernameChanged(username: String){
        _editUserInfoState.value = _editUserInfoState.value.copy(
            username = username,
            isUsernameValid = username.isNotBlank()
        )
    }

    fun onNameChanged(name: String){
        _editUserInfoState.value = _editUserInfoState.value.copy(
            name = name,
            isNameValid = name.isNotBlank()
        )
    }

    fun onSurnameChanged(surname: String){
        _editUserInfoState.value = _editUserInfoState.value.copy(
            surname = surname,
            isSurnameValid = surname.isNotBlank()
        )
    }

    private val _errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("EDIT_USER_INFO_VIEW_MODEL", e.toString(), e)
    }

    fun updateUserInfo(id : String){
        viewModelScope.launch (_errorHandler){
            _editUserInfoRequestState.emit(RequestState.LOADING)
            _userRepository.putUserInfo(
                UserInfoUpdateRequest(
                    id,
                    editUserInfoState.value.username,
                    editUserInfoState.value.name,
                    editUserInfoState.value.surname
                )
            ).onSuccess {
                _editUserInfoRequestState.emit(RequestState.SUCCESS)
                _editUserInfoState = mutableStateOf(
                    EditUserInfoState()
                )
                _authenticationRepository.removeAuthenticatedUser()
            }.onFailure {
                if(it is HttpException){
                    val error : ApiException = ErrorUtils().parseError(it)
                    _editUserInfoRequestState.emit(RequestState.FAILURE(error.title))
                }
                else
                    _editUserInfoRequestState.emit(RequestState.FAILURE(it.localizedMessage))
            }
        }
    }
}