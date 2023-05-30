package com.msomodi.imageverse.view.auth

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.msomodi.imageverse.R
import com.msomodi.imageverse.util.noRippleClickable
import com.msomodi.imageverse.view.common.RequestState
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun LoginScreen(
    modifier : Modifier = Modifier,
    loginState: LoginState,
    authenticationRequestState : MutableStateFlow<RequestState>,
    onLogin : () -> Unit,
    onRegister : () -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    context : Context
){
    val focusManager = LocalFocusManager.current

    var loadingState by rememberSaveable{
        mutableStateOf(false)
    }

    var passwordVisible by rememberSaveable{
        mutableStateOf(false)
    }

    val state by authenticationRequestState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = R.drawable.authentication),
                contentDescription = stringResource(R.string.image_of_a_person_authenticating_towards_a_system),
                modifier = modifier
                    .requiredHeight(200.dp)
                    .fillMaxWidth(),
            )
        }
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(topStart = 50.dp))
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .background(MaterialTheme.colors.background)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = modifier.padding(top = 50.dp, start = 20.dp, end = 20.dp)
            ){
                Column(verticalArrangement = Arrangement.Center){
                    Text(
                        text = stringResource(id = R.string.login),
                        style = MaterialTheme.typography.h1
                    )
                    Row(
                        modifier = modifier.padding(top = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.or),
                            style = MaterialTheme.typography.h5
                        )
                        Text(
                            modifier = modifier
                                .noRippleClickable(onRegister)
                                .padding(start = 8.dp),
                            text = stringResource(R.string.join_imageverse),
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
                Column(
                    modifier = modifier.padding(top = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = loginState.email,
                        onValueChange = onEmailChanged,
                        label = { Text(text = stringResource(R.string.email_address))},
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = { focusManager.moveFocus(FocusDirection.Down)}
                        ),
                        isError = !loginState.isEmailValid,
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = stringResource(R.string.email_icon)
                            )
                        }
                    )
                    OutlinedTextField(
                        modifier = modifier.fillMaxWidth(),
                        value = loginState.password,
                        onValueChange = onPasswordChanged,
                        label = { Text(text = stringResource(R.string.password))},
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = stringResource(R.string.lock_icon)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = {
                                passwordVisible = !passwordVisible
                            }){
                                Icon(
                                    imageVector = if(passwordVisible) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                                    contentDescription = stringResource(R.string.password_visibility_icon)
                                )
                            }
                        },
                        visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
                    )
                    Button(
                        modifier = modifier
                            .fillMaxWidth(),
                        onClick = onLogin,
                        shape = RoundedCornerShape(15.dp),
                        enabled = loginState.isEmailValid && !loadingState,
                    ) {
                        if(loadingState){
                            CircularProgressIndicator(color = MaterialTheme.colors.primary)
                        }
                        else{
                            Text(text = stringResource(R.string.login))
                        }
                    }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = modifier
                            .height(2.dp)
                            .weight(2.5f)
                            .background(MaterialTheme.colors.primary))
                        Text(
                            text = AnnotatedString(stringResource(id = R.string.or)),
                            modifier = modifier
                                .weight(1f),
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                        )
                        Box(modifier = modifier
                            .height(2.dp)
                            .weight(2.5f)
                            .background(MaterialTheme.colors.primary))
                    }
                    OutlinedButton(
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colors.onBackground,
                        ),
                        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
                        modifier = modifier
                            .fillMaxWidth(),
                        onClick = { },
                        shape = RoundedCornerShape(15.dp),
                        enabled = !loadingState

                    ) {
                        Text(text = stringResource(R.string.log_in_with_google), fontWeight = FontWeight.SemiBold)
                    }
                    OutlinedButton(
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colors.onBackground
                        ),
                        border = BorderStroke(2.dp, MaterialTheme.colors.primary),
                        modifier = modifier
                            .fillMaxWidth(),
                        onClick = { },
                        shape = RoundedCornerShape(15.dp),
                        enabled = !loadingState,
                    ) {
                        Text(text = stringResource(R.string.log_in_with_github), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
    
    state.let { state ->
        if(state is RequestState.LOADING){
            focusManager.clearFocus()
            loadingState = true
        } else if (state is RequestState.FAILURE){
            loadingState = false;
            val message = state.message
            LaunchedEffect(key1 = message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}