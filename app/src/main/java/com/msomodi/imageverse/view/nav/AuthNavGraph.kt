package com.msomodi.imageverse.view.nav

import android.content.Context
import androidx.compose.material.AlertDialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.msomodi.imageverse.view.AuthScreen
import com.msomodi.imageverse.view.auth.LoginScreen
import com.msomodi.imageverse.view.auth.RegisterScreen
import com.msomodi.imageverse.view.auth.WelcomeScreen
import com.msomodi.imageverse.viewmodel.GoogleSignInViewModel
import com.msomodi.imageverse.viewmodel.LoginViewModel
import com.msomodi.imageverse.viewmodel.RegisterViewModel

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    context: Context,
    googleSignInViewModel: GoogleSignInViewModel,
){
    val onRegister : () -> Unit = {
        navController.navigate(AuthScreen.Register.route)
    }

    val onLogin : () -> Unit = {
        navController.navigate(AuthScreen.Login.route)
    }

    val onNavigateToWelcomeScreen : () -> Unit = {
        navController.popBackStack()
        navController.navigate(AuthScreen.Welcome.route)
    }

    val onSuccess : () -> Unit = {
        navController.popBackStack()
        navController.navigate(Graph.USER)
    }

    val onSuccessHigherPrivileges : () -> Unit = {
        navController.popBackStack()
        navController.navigate(Graph.ADMIN)
    }

    navigation(
        route = Graph.AUTH,
        startDestination = AuthScreen.Welcome.route
    ){
        composable(route = AuthScreen.Welcome.route){
            WelcomeScreen(
                onRegister = onRegister,
                onLogin = onLogin,
                onGuestLogin = {
                    navController.popBackStack()
                    navController.navigate(Graph.GUEST)
                }
            )
        }
        composable(route = AuthScreen.Login.route) {
            LoginScreen(
                onLogin = {
                          loginViewModel.logIn(
                              onSuccess = onSuccess,
                              onSuccessHigherPrivileges =onSuccessHigherPrivileges
                          )
                },
                loginState = loginViewModel.loginState.value,
                authenticationRequestState = loginViewModel.loginRequestState,
                onEmailChanged = { loginViewModel.onEmailChanged(it) },
                onPasswordChanged = { loginViewModel.onPasswordChanged(it) },
                context = context,
                onRegister = onRegister,
                googleSignInViewModel = googleSignInViewModel,
                onGoogleLogOn = {
                    googleUser, userExists ->
                    if(userExists)
                    {
                        loginViewModel.onAuthenticationProviderId(googleUser.id)
                        loginViewModel.onAuthenticationType(1)
                        loginViewModel.logIn(
                            onSuccess = onSuccess,
                            onSuccessHigherPrivileges = onSuccessHigherPrivileges
                        )
                        googleSignInViewModel.resetState()
                    }else{
                        registerViewModel.onGoogleUserRegister(
                            googleUser.surname ?: "",
                            googleUser.name ?: "",
                            googleUser.email ?: "",
                            googleUser.profileImage ?: "",
                            googleUser.id,
                            1
                        )
                        navController.popBackStack()
                        navController.navigate(AuthScreen.Register.route)
                    }
                }
            )
        }
        composable(route = AuthScreen.Register.route) {
            RegisterScreen(
                registerViewModel = registerViewModel,
                onLogin = onLogin,
                onRegister = {
                    registerViewModel.register(
                        onSuccess = onSuccess,
                        onSuccessHigherPrivileges =onSuccessHigherPrivileges
                    )
                },
                onNavigateToWelcomeScreen = onNavigateToWelcomeScreen,
                context = context
            )
        }
    }
}