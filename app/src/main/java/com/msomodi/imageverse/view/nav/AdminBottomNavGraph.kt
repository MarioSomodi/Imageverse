package com.msomodi.imageverse.view.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.msomodi.imageverse.view.BottomNavScreen
import com.msomodi.imageverse.view.main.PostsScreen
import com.msomodi.imageverse.view.main.ProfileScreen
import com.msomodi.imageverse.view.main.SearchScreen

@Composable
fun AdminBottomNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = BottomNavScreen.Posts.route){
        composable(route = BottomNavScreen.Posts.route){
            PostsScreen()
        }
        composable(route = BottomNavScreen.Search.route){
            SearchScreen()
        }
        composable(route = BottomNavScreen.Profile.route){
            ProfileScreen()
        }
        composable(route = BottomNavScreen.Users.route){
            ProfileScreen()
        }
    }
}