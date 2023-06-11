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
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowLeft
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.auth.response.UserResponse
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.view.common.Dialog
import com.msomodi.imageverse.viewmodel.userProfile.EditUserInfoViewModel

@Composable
fun EditUserInfoScreen(
    modifier: Modifier = Modifier,
    editUserInfoViewModel: EditUserInfoViewModel = hiltViewModel(),
    user: UserResponse,
    onBackPressed: () -> Unit
){
    val context = LocalContext.current;

    val editUserInfoState = editUserInfoViewModel.editUserInfoState.value

    val state by editUserInfoViewModel.editUserInfoRequestState.collectAsState(null)

    state.let { state ->
        when(state){
            is RequestState.FAILURE -> {
                val message = state.message
                LaunchedEffect(key1 = message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
            else -> {}
        }
    }

    LaunchedEffect(Unit){
        editUserInfoViewModel.setInitialValuesInState(user.username, user.name, user.surname)
    }

    val focusManager = LocalFocusManager.current

    val updateEnabled =
        editUserInfoState.isNameValid &&
        editUserInfoState.isSurnameValid &&
        editUserInfoState.isUsernameValid &&
        editUserInfoState.name != user.name ||
        editUserInfoState.surname != user.surname ||
        editUserInfoState.username != user.username

    var askForConfirmation by rememberSaveable{
        mutableStateOf(false)
    }

    if(askForConfirmation){
        Dialog(
            dialogOpen = true,
            onConfirm = { editUserInfoViewModel.updateUserInfo(user.id) },
            confirmText = stringResource(R.string.yes),
            title = stringResource(R.string.are_you_sure),
            bodyText = stringResource(R.string.after_updating_your_info_you_will_be_logged_out_for_security_reasons_do_you_confirm_this_action)
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
            value = editUserInfoState.name,
            onValueChange = {editUserInfoViewModel.onNameChanged(it)},
            label = { Text(text = stringResource(R.string.name)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down)}
            ),
            isError = !editUserInfoState.isNameValid,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Badge,
                    contentDescription = stringResource(R.string.badge_icon_depicting_basic_user_info)
                )
            }
        )
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = editUserInfoState.surname,
            onValueChange = { editUserInfoViewModel.onSurnameChanged(it) },
            label = { Text(text = stringResource(R.string.surname))},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down)}
            ),
            isError = !editUserInfoState.isSurnameValid,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Badge,
                    contentDescription = stringResource(R.string.badge_icon_depicting_basic_user_info)
                )
            }
        )
        OutlinedTextField(
            modifier = modifier.fillMaxWidth(),
            value = editUserInfoState.username,
            onValueChange = {editUserInfoViewModel.onUsernameChanged(it)},
            label = { Text(text = stringResource(R.string.username))},
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.clearFocus()}
            ),
            isError = !editUserInfoState.isUsernameValid,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Badge,
                    contentDescription = stringResource(R.string.badge_icon_depicting_basic_user_info)
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