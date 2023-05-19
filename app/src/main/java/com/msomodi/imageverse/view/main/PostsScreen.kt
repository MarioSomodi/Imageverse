package com.msomodi.imageverse.view.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun PostsScreen(modifier: Modifier = Modifier){
    val state = remember {
        MutableTransitionState(false).apply { 
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(
            animationSpec = tween(3000)
        )
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Posts", style = typography.h1)
        }        
    }
}