package com.msomodi.imageverse.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.msomodi.imageverse.R

sealed class BottomNavScreen(
    val route: String,
    val title: Int,
    val icon: ImageVector
) {
    object Posts: BottomNavScreen(
        route = "posts",
        title = R.string.postsScreenTitle,
        icon = Icons.Default.Article
    )
    object Profile: BottomNavScreen(
        route = "profile",
        title = R.string.profileScreenTitle,
        icon = Icons.Default.Person
    )
    object Search: BottomNavScreen(
        route = "search",
        title = R.string.searchScreenTitle,
        icon = Icons.Default.Article
    )
}