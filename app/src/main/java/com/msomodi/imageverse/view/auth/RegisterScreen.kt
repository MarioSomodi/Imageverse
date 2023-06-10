package com.msomodi.imageverse.view.auth

import android.content.Context
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.packages.response.PackageResponse
import com.msomodi.imageverse.util.noRippleClickable
import com.msomodi.imageverse.view.common.PackageCard
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.view.common.StepsProgressBar
import com.msomodi.imageverse.viewmodel.auth.RegisterViewModel

@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onNavigateToWelcomeScreen: () -> Unit,
    registerViewModel: RegisterViewModel,
    context : Context
){
    val focusManager = LocalFocusManager.current

    var loadingState by rememberSaveable{
        mutableStateOf(false)
    }

    val registerState : RegisterState = registerViewModel.registerState.value

    val state by registerViewModel.registerRequestState.collectAsState()

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

    state.let { state ->
        when(state){
            RequestState.LOADING -> {
                focusManager.clearFocus()
                loadingState = true
            }
            is RequestState.FAILURE -> {
                loadingState = false;
                val message = state.message
                LaunchedEffect(key1 = message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
            RequestState.SUCCESS -> {
                loadingState = false
            }
            else -> {}
        }
    }

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
                            onUsernameChanged = { registerViewModel.onUsernameChanged(it) },
                            onSurnameChanged = { registerViewModel.onSurnameChanged(it) },
                            onNameChanged = { registerViewModel.onNameChanged(it) }
                        )
                        1 -> AuthenticationDetailsForm(
                            registerState = registerState,
                            focusManager = focusManager,
                            onEmailChanged = { registerViewModel.onEmailChanged(it) },
                            onPasswordChanged = { registerViewModel.onPasswordChanged(it) }
                        )
                        2 -> PackageChoice(
                            focusManager = focusManager,
                            registerViewModel = registerViewModel,
                            context = context
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
                                registerState.isPackageIdValid &&
                                !loadingState
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

    if(registerState.authenticationType == 0){
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
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PackageChoice(
    modifier: Modifier = Modifier,
    focusManager: FocusManager,
    context : Context,
    registerViewModel: RegisterViewModel,
){
    val state by registerViewModel.registerDataCollectionRequestState.collectAsState()

    var loadingState by remember {
        mutableStateOf(false)
    }

    var failureOccurred by remember {
        mutableStateOf(false)
    }

    val packagesState : List<PackageResponse>? = registerViewModel.packages.observeAsState(initial = null).value
    var packagesList : List<PackageResponse> = listOf()
    if(packagesState != null){
        packagesList = packagesState
    }

    val listState = rememberLazyListState()
    if(loadingState){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
            Text(text = stringResource(R.string.loading_packages))
        }
    }else if(failureOccurred){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.fillMaxSize()
        ) {
            Text(text = stringResource(R.string.internet_connection_error_or_server_connection_error_occurred), textAlign = TextAlign.Center)
            Button(onClick = {registerViewModel.getPackages()}) {
                Text(text = stringResource(R.string.try_again))
            }
        }
    }
    else{
        LazyRow(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(packagesList){ packageResponse ->
                PackageCard(
                    packageObj = packageResponse,
                    onSelectPackage = {registerViewModel.onPackageIdChange(it)},
                    selectedPackageId = registerViewModel.registerState.value.packageId)
            }
        }
    }

    state.let { state ->
        when(state){
            RequestState.LOADING -> {
                focusManager.clearFocus()
                loadingState = true
            }
            is RequestState.FAILURE -> {
                loadingState = false
                failureOccurred = true
                val message = state.message
                LaunchedEffect(key1 = message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
            RequestState.SUCCESS -> {
                loadingState = false
                failureOccurred = false
            }
            else -> {}
        }
    }
}