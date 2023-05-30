package com.msomodi.imageverse.view.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.msomodi.imageverse.view.AdminScreen
import com.msomodi.imageverse.view.GuestScreen
import com.msomodi.imageverse.view.UserScreen
import com.msomodi.imageverse.viewmodel.AuthenticationViewModel
import com.msomodi.imageverse.viewmodel.LoginViewModel
import com.msomodi.imageverse.viewmodel.RegisterViewModel

@Composable
fun RootNavGraph(navController: NavHostController){
    val authenticationViewModel = viewModel<AuthenticationViewModel>()
    val loginViewModel = viewModel<LoginViewModel>()
    val registerViewModel = viewModel<RegisterViewModel>()
    val context = LocalContext.current;
    var startDestination = Graph.AUTH;

    authenticationViewModel.removeAuthenticatedUser();
    authenticationViewModel.getAuthenticatedUser();
    val authResult = authenticationViewModel.authenticatedUser.observeAsState(initial = null).value

    if(authResult != null){
        if(authResult.authenticatedUserId != -1 && authResult.user!!.isAdmin)
        {
            startDestination = Graph.ADMIN
        }else if(authResult.authenticatedUserId != -1){
            startDestination = Graph.USER
        }
        NavHost(
            navController = navController,
            startDestination = startDestination,
            route = Graph.ROOT ){
            authNavGraph(navController, loginViewModel, registerViewModel, context)
            composable(route = Graph.ADMIN){
                AdminScreen()
            }
            composable(route = Graph.USER){
                UserScreen()
            }
            composable(route = Graph.GUEST){
                GuestScreen()
            }
        }
    }
}