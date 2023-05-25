package com.msomodi.imageverse.view

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.msomodi.imageverse.view.auth.Roles
import com.msomodi.imageverse.view.nav.AdminBottomNavGraph
import com.msomodi.imageverse.view.nav.BottomBar

@Composable
fun AdminScreen () {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(
            navController = navController,
            role = Roles.ADMIN
        ) }
    ) {
        AdminBottomNavGraph(navController = navController)
    }
}
