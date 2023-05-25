package com.msomodi.imageverse.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msomodi.imageverse.repository.AuthenticationRepository
import com.msomodi.imageverse.view.main.TestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class test @Inject constructor(
    private val repository: AuthenticationRepository
) : ViewModel(){
    private val _testState = mutableStateOf(TestState())

    val testState: State<TestState>
        get() = _testState

    private val errorHandler = CoroutineExceptionHandler {_, e ->
        Log.e("Test_VIEWMODEL", e.toString(), e)
    }

    init {
//        viewModelScope.launch (errorHandler){
//            _testState.value = _testState.value.copy(
//                AuthenticationResponse = repository.postLogin(),
//                loading = false
//            )
//        }
    }
}