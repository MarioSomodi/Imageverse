package com.msomodi.imageverse.view

sealed class AuthScreen(val route: String) {
    object Login: AuthScreen(route = "login")
    object Register: AuthScreen(route = "register")
}