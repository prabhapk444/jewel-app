package com.example.sriramjewellers.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val BackgroundColor = Color(0xFFFFFfFE)

val HeadlineColor = Color(0xFF272343)

val ParagraphColor = Color(0xFF2D334A)

val ButtonColor = Color(0xFFFFD803)

val ButtonTextColor = Color(0xFF272343)

private val LightColors = lightColorScheme(
    primary = ButtonColor,
    onPrimary = ButtonTextColor,
    background = BackgroundColor,
    onBackground = ParagraphColor,
    surface = BackgroundColor,
    onSurface = ParagraphColor
)

private val DarkColors = darkColorScheme(
    primary = ButtonColor,
    onPrimary = ButtonTextColor,
    background = BackgroundColor,
    onBackground = ParagraphColor,
    surface = BackgroundColor,
    onSurface = ParagraphColor
)

@Composable
fun SriramJewellersTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        shapes = Shapes(),
        content = content
    )
}
