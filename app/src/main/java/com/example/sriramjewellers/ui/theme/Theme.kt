package com.example.sriramjewellers.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


val BackgroundColor = Color(0xFFFFFBF5)
val HeadlineColor = Color(0xFF3E2C2C)
val ParagraphColor = Color(0xFF5C4B4B)
val ButtonColor = Color(0xFFB8860B)
val ButtonTextColor = Color(0xFFFFFFFF)

// Light color scheme
private val LightColors = lightColorScheme(
    primary = ButtonColor,
    onPrimary = ButtonTextColor,
    background = BackgroundColor,
    onBackground = ParagraphColor,
    surface = BackgroundColor,
    onSurface = ParagraphColor,
    secondary = Color(0xFFD4AF37),
    onSecondary = Color.White,
)

// Dark color scheme
private val DarkColors = darkColorScheme(
    primary = ButtonColor,
    onPrimary = ButtonTextColor,
    background = Color(0xFF1B1B1B),
    onBackground = Color(0xFFEAEAEA),
    surface = Color(0xFF2A2A2A),
    onSurface = Color(0xFFEAEAEA),
    secondary = Color(0xFFD4AF37),
    onSecondary = Color.Black
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
