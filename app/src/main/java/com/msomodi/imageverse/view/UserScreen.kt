package com.msomodi.imageverse.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.model.common.Roles
import com.msomodi.imageverse.view.common.BottomBar
import com.msomodi.imageverse.view.common.TopBar
import com.msomodi.imageverse.view.nav.UserBottomNavGraph

@Composable
fun UserScreen (
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit,
    authResult: AuthenticationResponse,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val navigateToProfile : () -> Unit = {
        navController.navigate(BottomNavScreen.Profile.route)
    }

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                role = Roles.USER
            )
        },
        topBar = {
            TopBar(
                onLogOut,
                navBackStackEntry,
                authResult,
                navigateToProfile
            )
        }
    ) {
        UserBottomNavGraph(
            modifier = modifier
                .padding(it),
            navController = navController,
            authResult = authResult
        )
    }
}

