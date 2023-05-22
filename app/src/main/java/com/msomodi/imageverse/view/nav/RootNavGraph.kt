package com.msomodi.imageverse.view.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.msomodi.imageverse.view.GuestScreen
import com.msomodi.imageverse.view.UserScreen
import com.msomodi.imageverse.viewmodel.AuthenticationViewModel

@Composable
fun RootNavGraph(navController: NavHostController){
    val authenticationViewModel = viewModel<AuthenticationViewModel>()
    NavHost(navController = navController, startDestination = Graph.AUTH , route = Graph.ROOT ){
        authNavGraph(navController, authenticationViewModel)
        composable(route = Graph.USER){
            UserScreen()
        }
        composable(route = Graph.GUEST){
            GuestScreen()
        }
    }
}