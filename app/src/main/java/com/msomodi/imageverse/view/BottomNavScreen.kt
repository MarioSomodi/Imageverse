package com.msomodi.imageverse.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.common.Roles

sealed class BottomNavScreen(
    val route: String,
    val title: Int,
    val icon: ImageVector,
    val roles: Collection<Roles>
) {
    object Posts: BottomNavScreen(
        route = "posts",
        title = R.string.postsScreenTitle,
        icon = Icons.Default.Article,
        roles = listOf(Roles.ADMIN, Roles.USER, Roles.GUEST)
    )
    object Search: BottomNavScreen(
        route = "search",
        title = R.string.searchScreenTitle,
        icon = Icons.Default.Article,
        roles = listOf(Roles.ADMIN, Roles.USER)
    )
    object Users: BottomNavScreen(
        route = "users",
        title = R.string.users,
        icon = Icons.Default.People,
        roles = listOf(Roles.ADMIN)
    )
    object Profile: BottomNavScreen(
        route = "profile",
        title = R.string.profileScreenTitle,
        icon = Icons.Default.Person,
        roles = listOf(Roles.ADMIN, Roles.USER)
    )
}