package com.msomodi.imageverse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomodi.imageverse.model.auth.google.GoogleUser
import com.msomodi.imageverse.model.auth.response.PackageResponse
import com.msomodi.imageverse.repository.AuthenticationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleSignInViewModel @Inject constructor(
    private val _authenticationRepository: AuthenticationRepository,
) : ViewModel() {

    private var _userExists : MutableLiveData<Boolean?> = MutableLiveData()
    val userExists: LiveData<Boolean?>
        get() = _userExists

    private var _googleUser : MutableLiveData<GoogleUser> = MutableLiveData()
    val googleUser: LiveData<GoogleUser>
        get() = _googleUser

    fun checkIfUserLoggedInBefore(authenticationProviderId : String) {
        viewModelScope.launch {
            val exists : Boolean = _authenticationRepository.checkIfUserHasAccount(authenticationProviderId)
            _userExists.postValue(exists)
        }
    }

    fun resetState(){
        _googleUser.postValue(null)
        _userExists.postValue(null)
    }

    fun setGoogleUser(
        id : String,
        name : String?,
        surname : String?,
        email : String?,
        profileImage : String?
    ) {
        _googleUser.postValue(
            GoogleUser(
                id,
                name,
                surname,
                email,
                profileImage
            )
        )
    }
}