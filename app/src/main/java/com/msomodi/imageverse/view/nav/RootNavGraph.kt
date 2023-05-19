package com.msomodi.imageverse.view.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.msomodi.imageverse.view.GuestScreen
import com.msomodi.imageverse.view.UserScreen

@Composable
fun RootNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = Graph.AUTH , route = Graph.ROOT ){
        authNavGraph(navController)
        composable(route = Graph.USER){
            UserScreen()
        }
        composable(route = Graph.GUEST){
            GuestScreen()
        }
    }
}