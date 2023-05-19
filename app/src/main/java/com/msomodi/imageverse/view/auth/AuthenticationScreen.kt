package com.msomodi.imageverse.view.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.msomodi.imageverse.R

@Composable
fun AuthenticationScreen(
    modifier : Modifier = Modifier,
    icon : Int,
    onLogin : () -> Unit,
    onRegister : () -> Unit,
    onGuestLogin : () -> Unit,
){
    Column (
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        Text(
            text = "Auth",
            textAlign = TextAlign.Center
        )
        Button(
                content = { Text(text = stringResource(R.string.login))},
                onClick = onLogin
            )
        Button(
            content = { Text(text = stringResource(R.string.register))},
            onClick = onRegister
        )
        Button(
            content = { Text(text = stringResource(R.string.login_as_guest))},
            onClick = onGuestLogin
        )
    }
}
