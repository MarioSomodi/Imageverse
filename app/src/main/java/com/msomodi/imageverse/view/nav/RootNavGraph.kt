package com.msomodi.imageverse.view.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.msomodi.imageverse.view.AdminScreen
import com.msomodi.imageverse.view.GuestScreen
import com.msomodi.imageverse.view.UserScreen
import com.msomodi.imageverse.viewmodel.AuthenticationViewModel

@Composable
fun RootNavGraph(navController: NavHostController){
    val authenticationViewModel = viewModel<AuthenticationViewModel>()
    val context = LocalContext.current;
    NavHost(
        navController = navController,
        startDestination = Graph.AUTH ,
        route = Graph.ROOT ){
        authNavGraph(navController, authenticationViewModel, context)
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