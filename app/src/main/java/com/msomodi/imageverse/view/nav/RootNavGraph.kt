package com.msomodi.imageverse.view.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.msomodi.imageverse.view.AdminScreen
import com.msomodi.imageverse.view.GuestScreen
import com.msomodi.imageverse.view.UserScreen
import com.msomodi.imageverse.viewmodel.auth.AuthenticationViewModel
import com.msomodi.imageverse.viewmodel.auth.GoogleSignInViewModel
import com.msomodi.imageverse.viewmodel.auth.LoginViewModel
import com.msomodi.imageverse.viewmodel.auth.RegisterViewModel
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun RootNavGraph(navController: NavHostController){
    val authenticationViewModel = viewModel<AuthenticationViewModel>()
    val loginViewModel = viewModel<LoginViewModel>()
    val registerViewModel = viewModel<RegisterViewModel>()
    val googleSignInViewModel = viewModel<GoogleSignInViewModel>()
    val context = LocalContext.current;
    var startDestination = Graph.AUTH;

    authenticationViewModel.getAuthenticatedUser();
    val authResult = authenticationViewModel.authenticatedUser.observeAsState(initial = null).value

    val onLogOut : () -> Unit = {
        authenticationViewModel.removeAuthenticatedUser();
    }

    if(authResult != null){
        if(authResult.authenticatedUserId != -1 && authResult.user!!.isAdmin)
        {
            startDestination = Graph.ADMIN
        }else if(authResult.authenticatedUserId != -1){
            val currentMoment: Instant = Clock.System.now()
            val datetimeInUtc: LocalDateTime = currentMoment.toLocalDateTime(TimeZone.UTC)
            //If package was changed and today is the day it is to be valid but user has still not logged out log him out for package change to take effect
            if(authResult.user?.activePackageId == authResult.user?.previousPackageId &&
                datetimeInUtc.date >= LocalDateTime.parse(authResult.user?.packageValidFrom?.dropLast(1)+"0").date &&
                authResult.user?.userStatistics?.totalTimesUserRequestedPackageChange!! > 0
            ){
               onLogOut()
            }
            startDestination = Graph.USER
        }
        NavHost(
            navController = navController,
            startDestination = startDestination,
            route = Graph.ROOT ){
            authNavGraph(navController, loginViewModel, registerViewModel, context, googleSignInViewModel)
            composable(route = Graph.ADMIN){
                AdminScreen(onLogOut = onLogOut, authResult = authResult)
            }
            composable(route = Graph.USER){
                UserScreen(onLogOut = onLogOut, authResult = authResult)
            }
            composable(route = Graph.GUEST){
                GuestScreen {
                    navController.popBackStack()
                    navController.navigate(Graph.AUTH)
                }
            }
        }
    }
}