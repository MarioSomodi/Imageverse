package com.msomodi.imageverse.view.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.ExperimentalPagingApi
import com.msomodi.imageverse.view.BottomNavScreen
import com.msomodi.imageverse.view.NavScreen
import com.msomodi.imageverse.view.main.PostsScreen

@OptIn(ExperimentalPagingApi::class)
@Composable
fun GuestBottomNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
){
    NavHost(navController = navController, startDestination = BottomNavScreen.Posts.route){
        composable(route = BottomNavScreen.Posts.route){
            PostsScreen(navigateToEditPost = {})
        }
    }
}