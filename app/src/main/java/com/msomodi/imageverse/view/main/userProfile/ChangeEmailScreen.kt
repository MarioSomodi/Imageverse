package com.msomodi.imageverse.view.main.userProfile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.user.response.UserResponse
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.view.common.Dialog
import com.msomodi.imageverse.viewmodel.userProfile.ChangeEmailViewModel

@Composable
fun ChangeEmailScreen(
    modifier : Modifier = Modifier,
    changeEmailViewModel: ChangeEmailViewModel = hiltViewModel(),
    user : UserResponse,
    onBackPressed : () -> Unit
) {
    val context = LocalContext.current;

    val changeEmailState = changeEmailViewModel.changeEmailState.value

    LaunchedEffect(Unit){
        changeEmailViewModel.onEmailChanged(user.email)
    }

    val state by changeEmailViewModel.changeEmailRequestState.collectAsState(null)

    var askForConfirmation by rememberSaveable{
        mutableStateOf(false)
    }

    state.let { state ->
        when(state){
            is RequestState.FAILURE -> {
                val message = state.message
                LaunchedEffect(key1 = message) {
                    askForConfirmation = false
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
            else -> {}
        }
    }

    val focusManager = LocalFocusManager.current

    val updateEnabled =
            changeEmailState.isEmailValid &&
            changeEmailState.email != user.email


    if(askForConfirmation){
        Dialog(
            dialogOpen = true,
            onConfirm = { changeEmailViewModel.updateUserEmail(user.id, user.authenticationType) },
            confirmText = stringResource(R.string.yes),
            title = stringResource(R.string.are_you_sure),
            bodyText = stringResource(R.string.after_updating_your_info_you_will_be_logged_out_for_security_reasons_do_you_confirm_this_action),
            onDismiss = {askForConfirmation = false}
        )
    }

    IconButton(modifier = modifier.padding(10.dp), onClick = {onBackPressed()}) {
        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back arrow")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = changeEmailState.email,
            onValueChange = {changeEmailViewModel.onEmailChanged(it)},
            label = { Text(text = stringResource(R.string.email_address)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.clearFocus()}
            ),
            isError = !changeEmailState.isEmailValid,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = stringResource(R.string.email_icon)
                )
            }
        )
        Button(
            modifier = modifier
                .fillMaxWidth(),
            onClick = {
                askForConfirmation = true
            },
            shape = RoundedCornerShape(15.dp),
            enabled = updateEnabled
        ) {
            Text(text = stringResource(R.string.update))
        }
    }
}