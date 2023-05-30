package com.msomodi.imageverse.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = primary,
    onPrimary = Color.White,
    secondary = secondary,
//    primaryVariant = darkPurple,
//    secondaryVariant = darkRed
)

private val LightColorPalette = lightColors(
    primary = primary,
    secondary = secondary,
//    primaryVariant = darkPurple,
//    secondaryVariant = darkRed
)

@Composable
fun ImageverseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}