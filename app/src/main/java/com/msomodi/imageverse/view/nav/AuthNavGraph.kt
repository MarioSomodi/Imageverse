package com.msomodi.imageverse.view.nav

import android.content.Context
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.msomodi.imageverse.view.AuthScreen
import com.msomodi.imageverse.view.auth.LoginScreen
import com.msomodi.imageverse.view.auth.RegisterScreen
import com.msomodi.imageverse.viewmodel.AuthenticationViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    authenticationViewModel: AuthenticationViewModel,
    context : Context
){
    val guestLogin : () -> Unit = {
        navController.popBackStack()
        navController.navigate(Graph.GUEST)
    }
    navigation(
        route = Graph.AUTH,
        startDestination = AuthScreen.Login.route
    ){
        composable(route = AuthScreen.Login.route) {
            LoginScreen(
                onLogin = {
                          authenticationViewModel.logIn(
                              onSuccess = {
                                  navController.popBackStack()
                                  navController.navigate(Graph.USER)
                              },
                              onSuccessHigherPrivileges = {
                                  navController.popBackStack()
                                  navController.navigate(Graph.ADMIN)
                              },
                          )
                },
                onRegister = {
                    navController.popBackStack()
                    navController.navigate(AuthScreen.Register.route)
                },
                onGuestLogin = guestLogin,
                authenticationState = authenticationViewModel.authenticationState.value,
                authenticationRequestState = authenticationViewModel.authenticationRequestState,
                onEmailChanged = { authenticationViewModel.onEmailChanged(it) },
                onPasswordChanged = { authenticationViewModel.onPasswordChanged(it) },
                context = context
            )
        }
        composable(route = AuthScreen.Register.route) {
            RegisterScreen(
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