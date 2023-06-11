package com.msomodi.imageverse.viewmodel.userProfile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomodi.imageverse.exception.ErrorUtils
import com.msomodi.imageverse.model.auth.response.UserLimitResponse
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.model.exception.ApiException
import com.msomodi.imageverse.repository.UserLimitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class PackageInformationViewModel @Inject constructor(
    private val _userLimitRepository : UserLimitRepository,
) : ViewModel() {

    private val _errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("PACKAGE_INFORMATION_VIEW_MODEL", e.toString(), e)
    }

    private val _userLimit : MutableLiveData<UserLimitResponse?> = MutableLiveData()
    val userLimit: LiveData<UserLimitResponse?>
        get() = _userLimit

    private val _userLimitRequestState = MutableStateFlow<RequestState>(RequestState.START)

    val userLimitRequestState : Flow<RequestState>
        get() = _userLimitRequestState

    fun getUserLimit(id : String) = viewModelScope.launch(_errorHandler){
        _userLimitRequestState.emit(RequestState.LOADING)
        val currentMoment: Instant = Clock.System.now()
        val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)
        _userLimitRepository.getUserLimitOnDate(datetimeInUtc.date.toString(), id).onSuccess {
            _userLimit.postValue(it)
            _userLimitRequestState.emit(RequestState.SUCCESS)
        }.onFailure {
            if(it is HttpException){
                val error : ApiException = ErrorUtils().parseError(it)
                _userLimitRequestState.emit(RequestState.FAILURE(error.title, isApiError = true))
            }
            else
                _userLimitRequestState.emit(RequestState.FAILURE(it.localizedMessage))
        }
    }
}