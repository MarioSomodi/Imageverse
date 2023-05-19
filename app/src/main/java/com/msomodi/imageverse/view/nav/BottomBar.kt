package com.msomodi.imageverse.view.nav

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.msomodi.imageverse.view.BottomNavScreen
import com.msomodi.imageverse.view.auth.Roles

@Composable
fun BottomBar(navController: NavHostController, role: Roles){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomNavigation {
        BottomNavScreen::class.sealedSubclasses.forEach{
            if(it.objectInstance!!.roles.contains(role)){
                AddItem(
                    screen = it.objectInstance!!,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomNavScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
){
    BottomNavigationItem(
        icon = {
            Icon(imageVector = screen.icon, contentDescription = stringResource(id = screen.title) )
        },
        label = { Text(text = stringResource(id = screen.title)) },
        selected = currentDestination?.hierarchy?.any {it.route == screen.route} == true,
        onClick = { navController.navigate(screen.route) {
                popUpTo(navController.graph.startDestinationId)
                launchSingleTop = true
            }
        }
    )
}