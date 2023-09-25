package com.msomodi.imageverse.view.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.ExperimentalPagingApi
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.view.BottomNavScreen
import com.msomodi.imageverse.view.NavScreen
import com.msomodi.imageverse.view.main.AddPostScreen
import com.msomodi.imageverse.view.main.EditPostScreen
import com.msomodi.imageverse.view.main.PostsScreen
import com.msomodi.imageverse.view.main.userProfile.ProfileScreen
import com.msomodi.imageverse.view.main.SearchScreen

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPagingApi::class)
@Composable
fun UserBottomNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authResult: AuthenticationResponse
){
    var state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    NavHost(navController = navController, startDestination = BottomNavScreen.Posts.route){
        composable(route = BottomNavScreen.Posts.route){
            AnimatedVisibility(
                visibleState = state,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                PostsScreen(authResult = authResult, navigateToEditPost = {navController.navigate(NavScreen.EditPost.route + "/$it")})
            }
        }
        composable(route = BottomNavScreen.Search.route){
            AnimatedVisibility(
                visibleState = state,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                SearchScreen()
            }
        }
        composable(route = BottomNavScreen.Profile.route){
            AnimatedVisibility(
                visibleState = state,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ProfileScreen(authResult)
            }
        }
        composable(route = NavScreen.AddPost.route){
            AnimatedVisibility(
                visibleState = state,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                AddPostScreen(
                    userId = authResult.user?.id,
                    navigateToPostsOnSuccess = {navController.navigate(BottomNavScreen.Posts.route)}
                )
            }
        }
        composable(route = NavScreen.EditPost.route + "/{id}") { navBackStack ->
            val id = navBackStack.arguments?.getString("id")
            AnimatedVisibility(
                visibleState = state,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                EditPostScreen(postId = id, navigateToPostsOnSuccess = { navController.navigate(BottomNavScreen.Posts.route)})
            }
        }
    }
}