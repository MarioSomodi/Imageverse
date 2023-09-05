package com.msomodi.imageverse.view

sealed class NavScreen(val route: String) {
    object AddPost: NavScreen(route = "addPost")
    object EditPost: NavScreen(route = "editPost")
}