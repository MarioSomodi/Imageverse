package com.msomodi.imageverse.view

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.msomodi.imageverse.view.auth.Roles
import com.msomodi.imageverse.view.nav.BottomBar
import com.msomodi.imageverse.view.nav.UserBottomNavGraph

@Composable
fun UserScreen () {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(
            navController = navController,
            role = Roles.USER
        ) }
    ) {
        UserBottomNavGraph(navController = navController)
    }
}

