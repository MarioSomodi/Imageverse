package com.msomodi.imageverse.view.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
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
fun AdminBottomNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    authResult: AuthenticationResponse
){
    NavHost(navController = navController, startDestination = BottomNavScreen.Posts.route){
        composable(route = BottomNavScreen.Posts.route){
            PostsScreen(authResult = authResult, navigateToEditPost = {navController.navigate(NavScreen.EditPost.route)})
        }
        composable(route = BottomNavScreen.Search.route){
            SearchScreen()
        }
        composable(route = BottomNavScreen.Profile.route){
            ProfileScreen(authResult)
        }
        composable(route = BottomNavScreen.Users.route){
            ProfileScreen(authResult)
        }
        composable(route = NavScreen.AddPost.route){
            AddPostScreen(
                userId = authResult.user?.id,
                navigateToPostsOnSuccess = {navController.navigate(BottomNavScreen.Posts.route)}
            )
        }
        composable(route = NavScreen.EditPost.route + "/{id}") { navBackStack ->
            val id = navBackStack.arguments?.getString("id")
            EditPostScreen(postId = id, navigateToPostsOnSuccess = { navController.navigate(BottomNavScreen.Posts.route)})
        }
    }
}