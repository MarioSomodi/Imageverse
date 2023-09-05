package com.msomodi.imageverse.view.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.util.noRippleClickable
import com.msomodi.imageverse.view.BottomNavScreen

@Composable
fun TopBar(
    onLogOut: () -> Unit,
    navigateToAddPost: () -> Unit,
    navBackStackEntry: NavBackStackEntry?,
    authResult: AuthenticationResponse?,
    navigateToProfile : () -> Unit,
    modifier : Modifier = Modifier,
){
    var title : Int = R.string.app_name
    var currentRoute : String = "";
    BottomNavScreen::class.sealedSubclasses.forEach{
        if(navBackStackEntry != null && navBackStackEntry.destination.route == it.objectInstance!!.route){
            title = it.objectInstance!!.title
            currentRoute = navBackStackEntry.destination.route!!
        }
    }
    
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        title = {
            Text(
                modifier = modifier.fillMaxWidth(),
                text = stringResource(id = title),
                style = MaterialTheme.typography.h4,
                textAlign = TextAlign.Center)
        },
        actions = {
            if(currentRoute == "posts" && authResult != null){
                TopAppBarActionButton(
                    imageVector = Icons.Outlined.Add,
                    description = stringResource(R.string.add_new_post),
                    onClick = navigateToAddPost
                )
            }
            TopAppBarActionButton(
                imageVector = Icons.Outlined.Logout,
                description = stringResource(R.string.log_out),
                onClick = onLogOut
            )
        },
        navigationIcon = {
            if(title == R.string.profileScreenTitle){
                Box{}
            }else if (authResult != null){
                    Box(modifier = modifier
                        .padding(8.dp)
                        .noRippleClickable {
                            navigateToProfile()
                        }
                    ){
                        AsyncImage(
                            modifier = modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .border(1.dp, Color.White, CircleShape),
                            model = authResult.user?.profileImage,
                            contentDescription = stringResource(R.string.profile_image),
                        )
                    }
                }
            }
    )
}

@Composable
fun TopAppBarActionButton(
    modifier : Modifier = Modifier,
    imageVector: ImageVector,
    description: String,
    onClick: () -> Unit
) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(
            tint = MaterialTheme.colors.onPrimary,
            modifier = modifier.size(28.dp),
            imageVector = imageVector,
            contentDescription = description
        )
    }
}