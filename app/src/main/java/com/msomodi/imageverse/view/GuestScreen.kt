package com.msomodi.imageverse.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.msomodi.imageverse.model.common.Roles
import com.msomodi.imageverse.view.common.BottomBar
import com.msomodi.imageverse.view.nav.GuestBottomNavGraph
import com.msomodi.imageverse.view.common.TopBar

@Composable
fun GuestScreen(
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit,
){
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                role = Roles.GUEST
            )
        },
        topBar = {
            TopBar(
                onLogOut,
                navBackStackEntry,
                null,
                {}
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