package com.msomodi.imageverse.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.msomodi.imageverse.view.auth.Roles
import com.msomodi.imageverse.view.nav.BottomBar
import com.msomodi.imageverse.view.nav.TopBar
import com.msomodi.imageverse.view.nav.UserBottomNavGraph

@Composable
fun UserScreen (
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(
                navController = navController,
                role = Roles.USER
            )
        },
        topBar = {
            TopBar(onLogOut)
        }
    ) {
        UserBottomNavGraph(
            modifier = modifier
                .padding(it),
            navController = navController
        )
    }
}

