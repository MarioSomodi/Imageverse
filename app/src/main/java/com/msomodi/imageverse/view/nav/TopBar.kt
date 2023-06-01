package com.msomodi.imageverse.view.nav

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.msomodi.imageverse.R

@Composable
fun TopBar(
    onLogOut : () -> Unit
){
    TopAppBar(
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onPrimary,
        title = {
            Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.h4)
        },
        actions = {
            TopAppBarActionButton(
                imageVector = Icons.Outlined.Logout,
                description = "Log out",
                onClick = onLogOut
            )
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