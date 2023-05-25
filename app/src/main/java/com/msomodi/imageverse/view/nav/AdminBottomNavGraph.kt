package com.msomodi.imageverse.view.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.msomodi.imageverse.view.BottomNavScreen
import com.msomodi.imageverse.view.main.PostsScreen
import com.msomodi.imageverse.view.main.ProfileScreen
import com.msomodi.imageverse.view.main.SearchScreen
import com.msomodi.imageverse.viewmodel.test

@Composable
fun AdminBottomNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = BottomNavScreen.Posts.route){
        composable(route = BottomNavScreen.Posts.route){
            val testVM = hiltViewModel<test>()
            PostsScreen(testState = testVM.testState.value)
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