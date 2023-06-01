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
import com.msomodi.imageverse.model.auth.request.RegisterRequest
import com.msomodi.imageverse.model.auth.response.PackageResponse
import com.msomodi.imageverse.model.exception.ApiException
import com.msomodi.imageverse.repository.AuthenticationRepository
import com.msomodi.imageverse.repository.PackageRepository
import com.msomodi.imageverse.util.isValidPassword
import com.msomodi.imageverse.view.auth.RegisterState
import com.msomodi.imageverse.view.common.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val _authenticationRepository : AuthenticationRepository,
    private val _packageRepository : PackageRepository
) : ViewModel() {
    private var _registerState = mutableStateOf(
        RegisterState()
    )
    val registerState: State<RegisterState>
        get() = _registerState

    private val _packages : MutableLiveData<List<PackageResponse>?> = MutableLiveData()
    val packages: LiveData<List<PackageResponse>?>
        get() = _packages

    val registerDataCollectionRequestState = MutableStateFlow<RequestState>(RequestState.START)

    val registerRequestState = MutableStateFlow<RequestState>(RequestState.START)

    private val _errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("REGISTER_VIEW_MODEL", e.toString(), e)
    }

    fun getPackages() = viewModelScope.launch(_errorHandler){
        registerDataCollectionRequestState.emit(RequestState.LOADING)
        _packageRepository.getPackages().onSuccess {
            _packages.postValue(it)
            registerDataCollectionRequestState.emit(RequestState.SUCCESS)
        }.onFailure {
            if(it is HttpException){
                val error : ApiException = ErrorUtils().parseError(it)
                registerDataCollectionRequestState.emit(RequestState.FAILURE(error.title))
            }
            else
                registerDataCollectionRequestState.emit(RequestState.FAILURE(it.localizedMessage))
        }
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

    fun register(onSuccess: () -> Unit, onSuccessHigherPrivileges: () -> Unit){
        viewModelScope.launch (_errorHandler){
            registerRequestState.emit(RequestState.LOADING)
            _authenticationRepository.postRegister(
                RegisterRequest(
                    registerState.value.username,
                    registerState.value.name,
                    registerState.value.surname,
                    registerState.value.email,
                    registerState.value.password,
                    registerState.value.packageId
                )
            ).onSuccess {
                registerRequestState.emit(RequestState.SUCCESS)
                _registerState = mutableStateOf(
                    RegisterState()
                )
                if(it.user!!.isAdmin){
                    onSuccessHigherPrivileges()
                }else{
                    onSuccess()
                }
            }.onFailure {
                if(it is HttpException){
                    val error : ApiException = ErrorUtils().parseError(it)
                    registerRequestState.emit(RequestState.FAILURE(error.title))
                }
                else
                    registerRequestState.emit(RequestState.FAILURE(it.localizedMessage))
            }
        }
    }

    init {
        getPackages()
    }
}