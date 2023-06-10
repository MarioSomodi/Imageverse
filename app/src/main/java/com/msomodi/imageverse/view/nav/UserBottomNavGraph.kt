package com.msomodi.imageverse.view.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.view.BottomNavScreen
import com.msomodi.imageverse.view.main.PostsScreen
import com.msomodi.imageverse.view.main.ProfileScreen
import com.msomodi.imageverse.view.main.SearchScreen
import com.msomodi.imageverse.viewmodel.profile.ProfileViewModel

@Composable
fun UserBottomNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authResult: AuthenticationResponse
){

    NavHost(navController = navController, startDestination = BottomNavScreen.Posts.route){
        composable(route = BottomNavScreen.Posts.route){
            PostsScreen()
        }
        composable(route = BottomNavScreen.Search.route){
            SearchScreen()
        }
        composable(route = BottomNavScreen.Profile.route){
            ProfileScreen(authResult)
        }
    }
}