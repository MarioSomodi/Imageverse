package com.msomodi.imageverse.view

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.msomodi.imageverse.view.auth.Roles
import com.msomodi.imageverse.view.nav.BottomBar
import com.msomodi.imageverse.view.nav.GuestBottomNavGraph

@Composable
fun GuestScreen(){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomBar(
            navController = navController,
            role = Roles.GUEST
        ) }
    ) {
        GuestBottomNavGraph(navController = navController)
    }
}