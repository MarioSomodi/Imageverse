package com.msomodi.imageverse.view.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.msomodi.imageverse.view.BottomNavScreen
import com.msomodi.imageverse.view.main.PostsScreen
import com.msomodi.imageverse.viewmodel.test

@Composable
fun GuestBottomNavGraph(navController: NavHostController){
    NavHost(navController = navController, startDestination = BottomNavScreen.Posts.route){
        composable(route = BottomNavScreen.Posts.route){
            val testVM = hiltViewModel<test>()
            PostsScreen(testState = testVM.testState.value)
        }
    }
}