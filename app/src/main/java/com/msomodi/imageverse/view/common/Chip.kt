package com.msomodi.imageverse.view.common

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun Chip(
    modifier : Modifier = Modifier,
    icon: ImageVector,
    tintColor: Color,
    isSelected: Boolean,
    text: String,
    onChecked: (Boolean) -> Unit,
    selectedColor: Color = Black,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
) {
    val shape = CircleShape

    val colorAnimatable = remember { Animatable(if (isSelected) selectedColor else Color.Transparent) }

    LaunchedEffect(isSelected) {
        colorAnimatable.animateTo(if (isSelected) selectedColor else Color.Transparent, animationSpec = tween(300))
    }

    CompositionLocalProvider(LocalLayoutDirection provides layoutDirection) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = if (isSelected) selectedColor else LightGray,
                    shape = shape
                )
                .background(
                    color = colorAnimatable.value,
                    shape = shape
                )
                .clip(shape = shape)
                .clickable {
                    onChecked(!isSelected)
                }
                .padding(vertical = 5.dp, horizontal = 10.dp),
        ) {
            Icon(
                imageVector = icon,
                tint = if (isSelected) White else tintColor,
                contentDescription = null
            )
            Text(
                text = text,
                color = if (isSelected) White else Unspecified
            )
        }
    }
}