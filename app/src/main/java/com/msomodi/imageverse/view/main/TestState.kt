package com.msomodi.imageverse.view.main

import com.msomodi.imageverse.model.auth.response.AuthenticationResponse

data class TestState(
    val AuthenticationResponse: AuthenticationResponse = AuthenticationResponse(null, null),
    val loading: Boolean = true
)