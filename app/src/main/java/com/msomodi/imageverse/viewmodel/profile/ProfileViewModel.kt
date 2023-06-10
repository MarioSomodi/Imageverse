package com.msomodi.imageverse.viewmodel.profile

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomodi.imageverse.exception.ErrorUtils
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.model.exception.ApiException
import com.msomodi.imageverse.model.packages.response.PackageResponse
import com.msomodi.imageverse.repository.PackageRepository
import com.msomodi.imageverse.view.main.ProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val _packageRepository : PackageRepository
) : ViewModel() {
    private var _profileState = mutableStateOf(
        ProfileState()
    )

    val activePackage: LiveData<PackageResponse?>
        get() = _profileState.value.activePackage

    val previousPackage: LiveData<PackageResponse?>
        get() = _profileState.value.activePackage

    private val profileDataCollectionRequestState = MutableStateFlow<RequestState>(RequestState.START)

    private val _errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("PROFILE_VIEW_MODEL", e.toString(), e)
    }

    fun getUsersPackages(activePackageId : String, previousPackageId : String) = viewModelScope.launch(_errorHandler){
        profileDataCollectionRequestState.emit(RequestState.LOADING)
        _packageRepository.getPackage(activePackageId).onSuccess {
            _profileState.value.activePackage.postValue(it)
        }.onFailure {
            handleError(it)
        }
        _packageRepository.getPackage(previousPackageId).onSuccess {
            _profileState.value.previousPackage.postValue(it)
        }.onFailure {
            handleError(it)
        }
        if(_profileState.value.activePackage.value != null && _profileState.value.previousPackage.value != null){
            profileDataCollectionRequestState.emit(RequestState.SUCCESS)
        }
    }

    private fun handleError(e : Throwable){
        viewModelScope.launch(_errorHandler){
            if(e is HttpException){
                val error : ApiException = ErrorUtils().parseError(e)
                profileDataCollectionRequestState.emit(RequestState.FAILURE(error.title))
            }
            else
                profileDataCollectionRequestState.emit(RequestState.FAILURE(e.localizedMessage))
        }
    }
}