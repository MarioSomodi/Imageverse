package com.msomodi.imageverse.view.auth

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msomodi.imageverse.R
import com.msomodi.imageverse.ui.theme.ImageverseTheme

@Composable
fun WelcomeScreen(
    modifier : Modifier = Modifier,
    onRegister: () -> Unit,
    onLogin: () -> Unit,
    onGuestLogin: () -> Unit
) {
    var isBouncing by remember { mutableStateOf(true) }

    val bounceAnimation = rememberInfiniteTransition()
    val bounceScale by bounceAnimation.animateFloat(
        initialValue = 1.0f,
        targetValue = if (isBouncing) 1.05f else 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(15.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.Center){
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = typography.h1
                )
                Text(
                    text = stringResource(R.string.are_you_ready_to_enter_the_imageverse),
                    style = typography.h4
                )
            }
        }
        Column(
            modifier = modifier
                .fillMaxSize(),
        ){
            Row(modifier = modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.photo),
                    contentDescription = stringResource(R.string.image),
                    modifier = modifier
                        .requiredHeight(130.dp)
                        .fillMaxWidth(0.50f)
                        .scale(bounceScale),
                )
            }
            Row(modifier = modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.photo_feed),
                    contentDescription = stringResource(R.string.image_of_an_photo_feed),
                    modifier = modifier
                        .requiredHeight(130.dp)
                        .fillMaxWidth(0.50f)
                        .scale(bounceScale),
                )
            }
            Row(modifier = modifier
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.photo_sharing),
                    contentDescription = stringResource(R.string.image_of_a_person_sharing_a_photo_to_socials),
                    modifier = modifier
                        .requiredHeight(130.dp)
                        .fillMaxWidth(0.50f)
                        .scale(bounceScale),
                )
            }
            Column(
                modifier = modifier
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = modifier
                        .fillMaxWidth(),
                    onClick = onRegister,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text = stringResource(R.string.register))
                }
                Button(
                    modifier = modifier
                        .fillMaxWidth(),
                    onClick = onLogin,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text = stringResource(R.string.login))
                }
                Button(
                    modifier = modifier
                        .fillMaxWidth(),
                    onClick = onGuestLogin,
                    shape = RoundedCornerShape(15.dp)
                ) {
                    Text(text = stringResource(R.string.login_as_guest))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WelcomeScreenPreview(){
    ImageverseTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            WelcomeScreen(onRegister = {}, onLogin = { }, onGuestLogin = {})
        }
    }
}