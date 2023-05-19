package com.msomodi.imageverse.view.nav

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.msomodi.imageverse.view.AuthScreen
import com.msomodi.imageverse.view.auth.AuthenticationScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController){
    val guestLogin : () -> Unit = {
        navController.popBackStack()
        navController.navigate(Graph.GUEST)
    }
    navigation(
        route = Graph.AUTH,
        startDestination = AuthScreen.Login.route
    ){
        composable(route = AuthScreen.Login.route) {
            AuthenticationScreen(
                icon = 0,
                onLogin = {
                    navController.popBackStack()
                    navController.navigate(Graph.USER)
                },
                onRegister = {
                    navController.popBackStack()
                    navController.navigate(AuthScreen.Register.route)
                },
                onGuestLogin = guestLogin
            )
        }
        composable(route = AuthScreen.Register.route) {
            AuthenticationScreen(
                icon = 0,
                onLogin = {
                    navController.popBackStack()
                    navController.navigate(AuthScreen.Login.route)
                },
                onRegister = {
                    navController.popBackStack()
                    navController.navigate(Graph.USER)
                },
                onGuestLogin = guestLogin
            )
        }
    }
}