package com.msomodi.imageverse.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.msomodi.imageverse.view.auth.Roles
import com.msomodi.imageverse.view.nav.BottomBar
import com.msomodi.imageverse.view.nav.GuestBottomNavGraph
import com.msomodi.imageverse.view.nav.TopBar

@Composable
fun GuestScreen(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit,
){
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                role = Roles.GUEST
            )
        },
        topBar = {
            TopBar(
                onLogOut = onLogOut
            )
        }
    ) {
        GuestBottomNavGraph(
            modifier = modifier
                .padding(it),
            navController = navController
        )
    }
}