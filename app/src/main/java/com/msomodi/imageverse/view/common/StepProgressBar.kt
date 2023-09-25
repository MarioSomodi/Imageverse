package com.msomodi.imageverse.view.common

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.msomodi.imageverse.R
import com.msomodi.imageverse.ui.theme.ImageverseTheme
import kotlin.math.roundToInt

@Composable
fun StepsProgressBar(
    modifier: Modifier = Modifier,
    numberOfSteps: Int,
    textColor : Color = MaterialTheme.colors.onPrimary,
    stepLabels : Collection<String>,
    currentStep: Int,
    allConditionsForCompletionValid : Boolean
) {
    var percentageDone : Int = (((currentStep).toFloat() / (numberOfSteps+1).toFloat()) * 100).roundToInt()
    if(allConditionsForCompletionValid)
       percentageDone = 100
    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ){
            Text(
                text = "$percentageDone%",
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                style = typography.h5
            )
            Text(
                text = stringResource(R.string.complete),
                color = Color.LightGray,
                fontWeight = FontWeight.Normal,
                style = typography.h6
            )
        }
        Text(
            text = stepLabels.elementAt(currentStep),
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            style = typography.h5
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            for (step in 0..numberOfSteps) {
                Step(
                    modifier = modifier
                        .weight(1F),
                    isComplete = step < currentStep,
                    isCurrent = step == currentStep
                )
            }
        }
    }
}

@Composable
fun Step(
    modifier: Modifier = Modifier,
    isComplete: Boolean,
    isCurrent: Boolean
) {
    val defaultColor = Color.White
    val completeColor = MaterialTheme.colors.secondary
    val currentColor = MaterialTheme.colors.secondary

    val color = when {
        isComplete -> completeColor
        isCurrent -> currentColor
        else -> defaultColor
    }

    val animatedColor = animateColorAsState(
        targetValue = color,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(animatedColor.value)
            .height(4.dp)
            .fillMaxWidth()
    )
}

@Preview
@Composable
fun StepsProgressBarPreview() {
    val currentStep = remember { mutableStateOf(2) }
    val stepLabels : Collection<String> = listOf("Personal details", "Authentication details", "Package choice")
    ImageverseTheme {
        StepsProgressBar(modifier = Modifier.fillMaxWidth(), numberOfSteps = 2,stepLabels = stepLabels, currentStep = currentStep.value, allConditionsForCompletionValid = false)
    }
}