package com.msomodi.imageverse.view.main.userProfile

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.TransitEnterexit
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.userLimit.response.UserLimitResponse
import com.msomodi.imageverse.model.user.response.UserResponse
import com.msomodi.imageverse.model.common.RequestState
import com.msomodi.imageverse.model.packages.response.PackageResponse
import com.msomodi.imageverse.view.common.StatisticsCard
import com.msomodi.imageverse.viewmodel.userProfile.PackageInformationViewModel

@Composable
fun PackageInformationScreen(
    modifier : Modifier = Modifier,
    currentPacakge : PackageResponse,
    user : UserResponse,
    packageInformationViewModel: PackageInformationViewModel = hiltViewModel()
){
    val context = LocalContext.current

    LaunchedEffect(Unit){
        packageInformationViewModel.getUserLimit(user.id)
    }

    val userLimitRequestState by packageInformationViewModel.userLimitRequestState.collectAsState(null)

    val userLimitState : UserLimitResponse? = packageInformationViewModel.userLimit.observeAsState(initial = null).value


    var loadingState by remember {
        mutableStateOf(false)
    }

    var failureOccurred by remember {
        mutableStateOf(false)
    }

    userLimitRequestState.let { state ->
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        if (loadingState) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
                Text(text = stringResource(R.string.loading_package_information))
            }
        } else if (failureOccurred) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.internet_connection_error_or_server_connection_error_occurred),
                    textAlign = TextAlign.Center
                )
                Button(onClick = { packageInformationViewModel.getUserLimit(user.id) }) {
                    Text(text = stringResource(R.string.try_again))
                }
            }
        } else if(userLimitState != null) {
            Text(text = "Max size allowed for images you can upload is ${currentPacakge.uploadSizeLimit}MB", textAlign = TextAlign.Center)
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatisticsCard(
                    icon = Icons.Outlined.Upload,
                    statisticValue = "${userLimitState.amountOfMBUploaded}/${currentPacakge.dailyUploadLimit}",
                    title = R.string.mb_uploaded_daily,
                    biggerCard = true
                )
                StatisticsCard(
                    icon = Icons.Outlined.FileUpload,
                    statisticValue = "${userLimitState.amountOfImagesUploaded}/${currentPacakge.dailyImageUploadLimit}",
                    title = R.string.images_uploaded_daily,
                    biggerCard = true
                )
            }
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatisticsCard(
                    icon = Icons.Outlined.TransitEnterexit,
                    statisticValue = if(userLimitState.requestedChangeOfPackage) "Yes" else "No",
                    title = R.string.package_change_requested,
                    biggerCard = true
                )
            }
        }
    }
}