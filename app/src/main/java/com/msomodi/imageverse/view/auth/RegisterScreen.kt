package com.msomodi.imageverse.view.auth

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.auth.response.PackageResponse
import com.msomodi.imageverse.ui.theme.ImageverseTheme
import com.msomodi.imageverse.util.noRippleClickable
import com.msomodi.imageverse.view.common.PackageCard
import com.msomodi.imageverse.view.common.StepsProgressBar

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    registerState: RegisterState,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onNavigateToWelcomeScreen: () -> Unit,
    onNameChanged: (String) -> Unit,
    onSurnameChanged: (String) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPackageIdChanged: (String) -> Unit,
){
    val focusManager = LocalFocusManager.current

    val loadingState by rememberSaveable{
        mutableStateOf(false)
    }

    val allConditionsForCompletionValid =
            registerState.isEmailValid &&
            registerState.isPasswordValid &&
            registerState.isPackageIdValid &&
            registerState.isNameValid &&
            registerState.isSurnameValid &&
            registerState.isUsernameValid

    var currentStep by remember { mutableStateOf(0) }
    val numberOfSteps = 2
    val stepLabels : Collection<String> = listOf("Personal details", "Authentication details", "Package choice")

    BackHandler(enabled = true) {
        if(currentStep != 0){
            currentStep -= 1
        }else{
            onNavigateToWelcomeScreen()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.primary),
        verticalArrangement = Arrangement.Bottom
    ) {
        Box(modifier = modifier
            .padding(top = 25.dp, bottom = 25.dp, start = 30.dp, end = 30.dp)
        ){
            StepsProgressBar(
                modifier = modifier
                    .fillMaxWidth(),
                numberOfSteps = numberOfSteps,
                stepLabels = stepLabels,
                currentStep = currentStep,
                allConditionsForCompletionValid = allConditionsForCompletionValid
            )
        }
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = modifier
                    .padding(top = 25.dp, start = 30.dp, end = 30.dp)
                    .fillMaxSize()
            ){
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.13F),
                    verticalArrangement = Arrangement.Center
                ){
                    Text(
                        text = stringResource(id = R.string.register),
                        style = MaterialTheme.typography.h1
                    )
                    Row(
                        modifier = modifier.padding(top = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(R.string.already_a_member)+"?",
                            style = MaterialTheme.typography.h5
                        )
                        Text(
                            modifier = modifier
                                .noRippleClickable(onLogin)
                                .padding(start = 8.dp),
                            text = stringResource(R.string.login),
                            style = MaterialTheme.typography.h5,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
                Column(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.83F)
                        .padding(top = 20.dp, bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    when(currentStep){
                        0 ->  PersonalDetailsForm(
                            registerState = registerState,
                            focusManager = focusManager,
                            onUsernameChanged = onUsernameChanged,
                            onSurnameChanged = onSurnameChanged,
                            onNameChanged = onNameChanged
                        )
                        1 -> AuthenticationDetailsForm(
                            registerState = registerState,
                            focusManager = focusManager,
                            onEmailChanged = onEmailChanged,
                            onPasswordChanged = onPasswordChanged
                        )
                        2 -> PackageChoice(
                            registerState = registerState,
                            onPackageIdChanged = onPackageIdChanged
                        )
                    }
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(1F),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    if(currentStep != 0){
                        Button(
                            modifier = modifier
                                .fillMaxWidth(0.50F),
                            onClick = {
                                currentStep -= 1
                            },
                            shape = RoundedCornerShape(15.dp),
                        ) {
                            Text(text = stringResource(R.string.previous))
                        }
                    }
                    Button(
                        modifier =
                        if(currentStep != 0)
                        {
                            modifier
                                .fillMaxWidth(1f)}
                        else{
                            modifier
                                .fillMaxWidth()
                        },
                        onClick =
                        if(currentStep != numberOfSteps){
                            { currentStep += 1 }
                        }else{
                            onRegister
                        },
                        shape = RoundedCornerShape(15.dp),
                        enabled = currentStep == 0 &&
                                registerState.isNameValid &&
                                registerState.isUsernameValid &&
                                registerState.isSurnameValid ||
                                currentStep == 1 &&
                                registerState.isEmailValid &&
                                registerState.isPasswordValid ||
                                currentStep == 2 &&
                                registerState.isPackageIdValid
                    ) {
                        if(currentStep != numberOfSteps){
                            Text(text = stringResource(R.string.next))
                        }
                        else if (loadingState){
                            CircularProgressIndicator(color = MaterialTheme.colors.primary)
                        }
                        else{
                            Text(text = stringResource(R.string.register))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PersonalDetailsForm(
    modifier: Modifier = Modifier,
    registerState: RegisterState,
    focusManager: FocusManager,
    onNameChanged: (String) -> Unit,
    onSurnameChanged: (String) -> Unit,
    onUsernameChanged: (String) -> Unit,
){
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = registerState.name,
            onValueChange = onNameChanged,
            label = { Text(text = stringResource(R.string.name))},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down)}
            ),
            isError = !registerState.isNameValid,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Badge,
                    contentDescription = stringResource(R.string.badge_icon_depicting_basic_user_info)
                )
            }
        )
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = registerState.surname,
            onValueChange = onSurnameChanged,
            label = { Text(text = stringResource(R.string.surname))},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down)}
            ),
            isError = !registerState.isSurnameValid,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Badge,
                    contentDescription = stringResource(R.string.badge_icon_depicting_basic_user_info)
                )
            }
        )
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = registerState.username,
            onValueChange = onUsernameChanged,
            label = { Text(text = stringResource(R.string.username))},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            ),
            isError = !registerState.isUsernameValid,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Badge,
                    contentDescription = stringResource(R.string.badge_icon_depicting_basic_user_info)
                )
            }
        )
}

@Composable
fun AuthenticationDetailsForm(
    modifier: Modifier = Modifier,
    registerState: RegisterState,
    focusManager: FocusManager,
    onPasswordChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
){
    var passwordVisible by rememberSaveable{
        mutableStateOf(false)
    }
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = registerState.email,
        onValueChange = onEmailChanged,
        label = { Text(text = stringResource(R.string.email_address))},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.moveFocus(FocusDirection.Down)}
        ),
        isError = !registerState.isEmailValid,
        leadingIcon = {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = stringResource(R.string.email_icon)
            )
        }
    )
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = registerState.password,
        onValueChange = onPasswordChanged,
        label = { Text(text = stringResource(R.string.password))},
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        isError = !registerState.isPasswordValid,
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PackageChoice(
    registerState: RegisterState,
    onPackageIdChanged: (String) -> Unit,
){
    val packageObj = PackageResponse(
        "1",
        "Default",
        9.99.toDouble(),
        10,
        10,
        10
    )
    val packageObj2 = PackageResponse(
        "2",
        "Default2",
        9.99.toDouble(),
        10,
        10,
        10
    )
    val packageObj3 = PackageResponse(
        "3",
        "Default3",
        9.99.toDouble(),
        10,
        10,
        10
    )

    val packageList : List<PackageResponse> = listOf(packageObj, packageObj2, packageObj3)

    val state = rememberLazyListState()

    LazyRow(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        state = state,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
        horizontalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(packageList){ packageResponse ->
            PackageCard(packageObj = packageResponse, onSelectPackage = {onPackageIdChanged(it)}, selectedPackageId = registerState.packageId)
        }
    }
}