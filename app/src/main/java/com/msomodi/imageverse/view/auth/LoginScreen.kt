package com.msomodi.imageverse.view.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.msomodi.imageverse.R

@Composable
fun LoginScreen(
    modifier : Modifier = Modifier,
    authenticationState: AuthenticationState,
    onLogin : () -> Unit,
    onRegister : () -> Unit,
    onGuestLogin : () -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit
){
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    val state = remember{
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(3000))
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ){
            Card(
                modifier = modifier
                    .padding(
                        vertical = 50.dp,
                        horizontal = 12.dp
                    )
                    .fillMaxWidth(),
                shape = RoundedCornerShape(size = 9.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colors.primary
                )
            ){
                Column(
                    modifier = modifier.padding(all = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    Text(
                        text = "Login",
                        textAlign = TextAlign.Center,
                        style = typography.h2
                    )
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = authenticationState.email,
                        onValueChange = onEmailChanged,
                        label = { Text(text = stringResource(R.string.email_address))},
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down)}
                        ),
                        isError = !authenticationState.isEmailValid
                    )
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = authenticationState.password,
                        onValueChange = onPasswordChanged,
                        label = { Text(text = stringResource(R.string.password))},
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        isError = !authenticationState.isPasswordValid,
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Button(
                        modifier = modifier.fillMaxWidth(),
                        content = { Text(text = stringResource(R.string.login))},
                        onClick = onLogin,
                        enabled = authenticationState.isEmailValid && authenticationState.isPasswordValid
                    )

                    Button(
                        modifier = modifier.fillMaxWidth(),
                        content = { Text(text = stringResource(R.string.register))},
                        onClick = onRegister
                    )

                    Button(
                        modifier = modifier.fillMaxWidth(),
                        content = { Text(text = stringResource(R.string.login_as_guest))},
                        onClick = onGuestLogin
                    )
                }
            }
        }
    }
}
