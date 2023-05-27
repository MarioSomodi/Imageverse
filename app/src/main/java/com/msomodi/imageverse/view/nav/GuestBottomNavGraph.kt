package com.msomodi.imageverse.view.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.msomodi.imageverse.view.BottomNavScreen
import com.msomodi.imageverse.view.main.PostsScreen

@Composable
fun GuestBottomNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = BottomNavScreen.Posts.route){
        composable(route = BottomNavScreen.Posts.route){
            PostsScreen()
        }
    }
}