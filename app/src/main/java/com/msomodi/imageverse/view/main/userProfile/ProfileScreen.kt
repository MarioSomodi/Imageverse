package com.msomodi.imageverse.view.main.userProfile

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentScope.SlideDirection.Companion.Up
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material.icons.outlined.TransitEnterexit
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.msomodi.imageverse.R
import com.msomodi.imageverse.model.auth.response.AuthenticationResponse
import com.msomodi.imageverse.model.packages.response.PackageResponse
import com.msomodi.imageverse.view.common.Chip
import com.msomodi.imageverse.view.common.StatisticsCard
import com.msomodi.imageverse.viewmodel.userProfile.ProfileViewModel
import kotlinx.datetime.LocalDateTime

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProfileScreen(
        authResult : AuthenticationResponse,
        modifier : Modifier = Modifier,
        profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val user = authResult.user!!

    var currentUpdateScreen by remember { mutableStateOf("")}

    LaunchedEffect(Unit){
        profileViewModel.getUsersPackages(user.activePackageId, user.previousPackageId)
    }

    val activePackage : PackageResponse? = profileViewModel.activePackage.observeAsState(null).value
    val previousPackage : PackageResponse? = profileViewModel.previousPackage.observeAsState(null).value

    var editProfileSelected by remember {
        mutableStateOf(false)
    }

    var packageSelected by remember {
        mutableStateOf(false)
    }

    val firstLogin = LocalDateTime.parse(user.userStatistics.firstLogin.dropLast(1)+"0")
    val lastLogin = LocalDateTime.parse(user.userStatistics.lastLogin.dropLast(1)+"0")

    Column(modifier.fillMaxSize()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier
                    .padding(start = 5.dp, end = 5.dp, top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                AsyncImage(
                    modifier = modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(4.dp, Color.White, CircleShape),
                    model = user.profileImage,
                    contentDescription = stringResource(R.string.profile_image),
                )
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "${user.name} ${user.surname}",
                        style = typography.h4,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "@${user.username} | ${activePackage?.name} user",
                        style = typography.h6,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
            Row(modifier = modifier
                .padding(horizontal = 10.dp, vertical = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ){
                Chip(
                    icon = Icons.Outlined.Edit,
                    tintColor = Color.White,
                    isSelected = editProfileSelected,
                    text = stringResource(R.string.edit_profile),
                    onChecked = {
                        editProfileSelected = !editProfileSelected
                        packageSelected = false
                    }
                )

                Chip(
                    icon = Icons.Outlined.CardGiftcard,
                    tintColor = Color.White,
                    isSelected = packageSelected,
                    text = stringResource(R.string.packageString),
                    onChecked = {
                        packageSelected = !packageSelected
                        editProfileSelected = false
                    }
                )
            }
        }
        if(editProfileSelected){
            if(currentUpdateScreen.isBlank()){
                Column(modifier = modifier
                    .fillMaxSize()
                    .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Button(
                        modifier = modifier
                            .fillMaxWidth(),
                        onClick = {
                            currentUpdateScreen = "user_info"
                        },
                        shape = RoundedCornerShape(15.dp),
                    ) {
                        Text(text = stringResource(R.string.edit_basic_info))
                    }
                    Button(
                        modifier = modifier
                            .fillMaxWidth(),
                        onClick = {
                            currentUpdateScreen = "user_email"

                        },
                        shape = RoundedCornerShape(15.dp),
                    ) {
                        Text(text = stringResource(R.string.change_email))
                    }
                    if(user.authenticationType == 0){
                        Button(
                            modifier = modifier
                                .fillMaxWidth(),
                            onClick = {
                                currentUpdateScreen = "user_password"
                            },
                            shape = RoundedCornerShape(15.dp),
                        ) {
                            Text(text = stringResource(R.string.change_password))
                        }
                    }
                    Button(
                        modifier = modifier
                            .fillMaxWidth(),
                        onClick = {
                            currentUpdateScreen = "user_package"
                        },
                        shape = RoundedCornerShape(15.dp),
                    ) {
                        Text(text = stringResource(R.string.change_package))
                    }
                }
            }else{
                AnimatedContent(
                    targetState = currentUpdateScreen,
                    transitionSpec = {
                        slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = Up
                        ).with(
                            slideOutOfContainer(
                                animationSpec = tween(300, easing = EaseOut),
                                towards = Down
                            )
                        )
                    }) {
                    targetState ->
                    when(targetState){
                        "user_info" -> EditUserInfoScreen(user = user, onBackPressed = {currentUpdateScreen = ""})
                        "user_email" -> ChangeEmailScreen(user = user, onBackPressed = {currentUpdateScreen = ""})
                        "user_password" -> ChangePasswordScreen(user = user, onBackPressed = {currentUpdateScreen = ""})
                        "user_package" -> ChangePackageScreen(user = user, onBackPressed = {currentUpdateScreen = ""})
                    }
                }

            }
        }else if(packageSelected){
            PackageInformationScreen(currentPacakge = activePackage!!, user = user)
        }else{
            Column(modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextField(
                    modifier = modifier.padding(top = 15.dp, bottom = 20.dp),
                    readOnly = true,
                    value = user.email,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            tint = Color.White,
                            contentDescription = "Email"
                        )
                    },
                    label = { Text(text = stringResource(id = R.string.email_address)) },
                    onValueChange = { }
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatisticsCard(
                        icon = Icons.Outlined.Timer,
                        statisticValue = firstLogin.date.toString(),
                        title = R.string.first_login
                    )
                    StatisticsCard(
                        icon = Icons.Outlined.Timer,
                        statisticValue = lastLogin.date.toString(),
                        title = R.string.last_login
                    )
                    StatisticsCard(
                        icon = Icons.Outlined.TransitEnterexit,
                        statisticValue = user.userStatistics.totalTimesLoggedIn.toString(),
                        title = R.string.times_logged_in
                    )
                }
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatisticsCard(
                        icon = Icons.Outlined.UploadFile,
                        statisticValue = user.userStatistics.totalImagesUploaded.toString(),
                        title = R.string.images_uploaded
                    )
                    StatisticsCard(
                        icon = Icons.Outlined.Edit,
                        statisticValue = user.userStatistics.totalTimesPostsWereEdited.toString(),
                        title = R.string.posts_edited
                    )
                    StatisticsCard(
                        icon = Icons.Outlined.Upload,
                        statisticValue = user.userStatistics.totalMbUploaded.toString(),
                        title = R.string.mb_uploaded
                    )
                }
            }
        }
    }
}

