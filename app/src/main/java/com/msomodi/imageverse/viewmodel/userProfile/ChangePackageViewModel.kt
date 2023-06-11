package com.msomodi.imageverse.viewmodel.userProfile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomodi.imageverse.exception.ErrorUtils
import com.msomodi.imageverse.model.auth.request.UserPackageUpdateRequest
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.model.exception.ApiException
import com.msomodi.imageverse.model.packages.response.PackageResponse
import com.msomodi.imageverse.repository.AuthenticationRepository
import com.msomodi.imageverse.repository.PackageRepository
import com.msomodi.imageverse.repository.UserRepository
import com.msomodi.imageverse.view.main.userProfile.ChangePackageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ChangePackageViewModel @Inject constructor(
    private val _userRepository : UserRepository,
    private val _authenticationRepository : AuthenticationRepository,
    private val _packageRepository : PackageRepository
) : ViewModel() {
    private var _changePackageState = mutableStateOf(
        ChangePackageState()
    )
    val changePackageState: State<ChangePackageState>
        get() = _changePackageState

    private val _packages : MutableLiveData<List<PackageResponse>?> = MutableLiveData()
    val packages: LiveData<List<PackageResponse>?>
        get() = _packages

    private val _changePackageRequestState = MutableStateFlow<RequestState>(RequestState.START)

    val changePackageRequestState : Flow<RequestState>
        get() = _changePackageRequestState

    private val _changePackageDataCollectionRequestState = MutableStateFlow<RequestState>(RequestState.START)

    val changePackageDataCollectionRequestState : Flow<RequestState>
        get() = _changePackageDataCollectionRequestState

    fun onPackageIdChanged(packageId: String){
        _changePackageState.value = _changePackageState.value.copy(
            packageId = packageId,
        )
    }

    private val _errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("CHANGE_PACKAGE_VIEW_MODEL", e.toString(), e)
    }

    fun getPackages() = viewModelScope.launch(_errorHandler){
        _changePackageDataCollectionRequestState.emit(RequestState.LOADING)
        _packageRepository.getPackages().onSuccess {
            _packages.postValue(it)
            _changePackageDataCollectionRequestState.emit(RequestState.SUCCESS)
        }.onFailure {
            if(it is HttpException){
                val error : ApiException = ErrorUtils().parseError(it)
                _changePackageDataCollectionRequestState.emit(RequestState.FAILURE(error.title, isApiError = true))
            }
            else
                _changePackageDataCollectionRequestState.emit(RequestState.FAILURE(it.localizedMessage))
        }
    }


    fun updateUserPackage(id : String){
        viewModelScope.launch (_errorHandler){
            _changePackageRequestState.emit(RequestState.LOADING)
            _userRepository.putUserPackage(
                UserPackageUpdateRequest(
                    id,
                    changePackageState.value.packageId,
                )
            ).onSuccess {
                _changePackageRequestState.emit(RequestState.SUCCESS)
                _changePackageState = mutableStateOf(
                    ChangePackageState()
                )
            }.onFailure {
                if(it is HttpException){
                    val error : ApiException = ErrorUtils().parseError(it)
                    _changePackageRequestState.emit(RequestState.FAILURE(error.title))
                }
                else
                    _changePackageRequestState.emit(RequestState.FAILURE(it.localizedMessage))
            }
        }
    }

    init {
        getPackages()
    }
}