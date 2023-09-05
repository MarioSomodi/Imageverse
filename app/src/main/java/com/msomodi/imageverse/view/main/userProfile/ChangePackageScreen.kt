package com.msomodi.imageverse.view.main.userProfile

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.user.response.UserResponse
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.model.packages.response.PackageResponse
import com.msomodi.imageverse.view.common.Dialog
import com.msomodi.imageverse.view.common.PackageCard
import com.msomodi.imageverse.viewmodel.userProfile.ChangePackageViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChangePackageScreen(
    modifier : Modifier = Modifier,
    user : UserResponse,
    onBackPressed : () -> Unit,
    changePackageViewModel: ChangePackageViewModel = hiltViewModel()
) {
    val context = LocalContext.current;

    val changePackageState = changePackageViewModel.changePackageState.value

    var loadingState by remember {
        mutableStateOf(false)
    }

    var failureOccurred by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit){
        changePackageViewModel.onPackageIdChanged(user.activePackageId)
    }

    val state by changePackageViewModel.changePackageRequestState.collectAsState(null)

    val statePackages by changePackageViewModel.changePackageDataCollectionRequestState.collectAsState(null)

    var askForConfirmation by rememberSaveable{
        mutableStateOf(false)
    }

    val updateEnabled =
                changePackageState.packageId != user.activePackageId

    val packagesState : List<PackageResponse>? = changePackageViewModel.packages.observeAsState(initial = null).value
    var packagesList : List<PackageResponse> = listOf()
    if(packagesState != null){
        packagesList = packagesState
    }

    if(askForConfirmation){
        Dialog(
            dialogOpen = true,
            onConfirm = { changePackageViewModel.updateUserPackage(user.id) },
            confirmText = stringResource(R.string.yes),
            title = stringResource(R.string.are_you_sure),
            bodyText = stringResource(R.string.package_change_will_be_valid_from_next_day_you_will_be_logged_out_tomorrow_for_security_reasons_as_well_as_for_package_changes_to_take_effect),
            onDismiss = {askForConfirmation = false}
        )
    }

    IconButton(modifier = modifier.padding(5.dp), onClick = {onBackPressed()}) {
        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Back arrow")
    }

    val listState = rememberLazyListState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
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
                Button(onClick = {changePackageViewModel.getPackages()}) {
                    Text(text = stringResource(R.string.try_again))
                }
            }
        }
        else{
            LazyRow(
                verticalAlignment = Alignment.CenterVertically,
                state = listState,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(packagesList){ packageResponse ->
                    PackageCard(
                        packageObj = packageResponse,
                        onSelectPackage = {changePackageViewModel.onPackageIdChanged(it)},
                        selectedPackageId = changePackageState.packageId,
                        smallerCard = true
                    )
                }
            }
        }
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

    statePackages.let { state ->
        when(state){
            RequestState.LOADING -> {
                loadingState = true
            }
            is RequestState.FAILURE -> {
                loadingState = false
                val message = state.message
                failureOccurred = !state.isApiError
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