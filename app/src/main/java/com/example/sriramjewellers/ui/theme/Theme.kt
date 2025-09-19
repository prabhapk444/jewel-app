package com.example.sriramjewellers.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color



val BackgroundColor = Color(0xFFFCFAF8)
val HeadlineColor = Color(0xFF1C1410)
val ParagraphColor = Color(0xFF4D3F33)

val ButtonColor = Color(0xFFD4AF37)

val ButtonTextColor = Color(0xFF0F0A07)



private val AccentGold = Color(0xFFE5D352)
private val DeepGold = Color(0xFFB8901F)
private val LuxuryBrown = Color(0xFF2A1E19)
private val SoftCream = Color(0xFFFFFDF9)
private val ErrorRed = Color(0xFFDC2626)
private val SuccessGreen = Color(0xFF059669)


private val RoseGold = Color(0xFFE8B4B8)
private val Platinum = Color(0xFFE5E7EB)
private val Diamond = Color(0xFFFDFEFF)
private val Onyx = Color(0xFF050404)
private val Pearl = Color(0xFFFBF8F3)
private val Copper = Color(0xFFBC6C25)


private val Amethyst = Color(0xFF9F7AEA)
private val Sapphire = Color(0xFF1E40AF)
private val Citrine = Color(0xFFFBBF24)
private val Moonstone = Color(0xFFE0E7FF)
private val Bronze = Color(0xFFCD7F32)
private val Silver = Color(0xFFC0C0C4)
private val LightColors = lightColorScheme(
    primary = ButtonColor,
    onPrimary = ButtonTextColor,
    primaryContainer = Color(0xFFFFF9E6),
    onPrimaryContainer = Color(0xFF241A0A),

    secondary = Bronze,
    onSecondary = Color(0xFFFFFEFD),
    secondaryContainer = Color(0xFFFFEDD5),
    onSecondaryContainer = Color(0xFF2D1F10),

    tertiary = RoseGold,
    onTertiary = Color(0xFF3D1F23),
    tertiaryContainer = Color(0xFFFFE4E6),
    onTertiaryContainer = Color(0xFF2D0F11),

    background = BackgroundColor,
    onBackground = ParagraphColor,
    surface = BackgroundColor,
    onSurface = ParagraphColor,
    surfaceVariant = Color(0xFFF8F5F2),
    onSurfaceVariant = Color(0xFF524338),

    outline = Color(0xFFD4C4B0),
    outlineVariant = Color(0xFFEBE0D6),

    error = ErrorRed,
    onError = Color(0xFFFFFEFD),
    errorContainer = Color(0xFFFEF2F2),
    onErrorContainer = Color(0xFF7F1D1D),

    surfaceTint = ButtonColor,
    inverseSurface = HeadlineColor,
    inverseOnSurface = Diamond,
    inversePrimary = AccentGold,

    scrim = Color(0x4D000000),
)


private val DarkColors = darkColorScheme(
    primary = AccentGold,
    onPrimary = Color(0xFF0A0806),
    primaryContainer = Color(0xFF3D3115),
    onPrimaryContainer = Color(0xFFFFF4D6),

    secondary = ButtonColor,
    onSecondary = Onyx,
    secondaryContainer = Color(0xFF4A3A1F),
    onSecondaryContainer = Color(0xFFFFE0A3),

    tertiary = Platinum,
    onTertiary = Color(0xFF1A1816),
    tertiaryContainer = Color(0xFF3F3C39),
    onTertiaryContainer = Color(0xFFF8F5F2),

    background = Color(0xFF0A0807),
    onBackground = Color(0xFFF8F5F0),
    surface = Color(0xFF121010),
    onSurface = Color(0xFFF8F5F0),
    surfaceVariant = Color(0xFF1F1B18),
    onSurfaceVariant = Color(0xFFE5D5C8),

    outline = Color(0xFFB8A68F),
    outlineVariant = Color(0xFF3D3229),

    error = Color(0xFFFF7878),
    onError = Color(0xFF0A0606),
    errorContainer = Color(0xFF4A1A1A),
    onErrorContainer = Color(0xFFFFE0E0),

    surfaceTint = AccentGold,
    inverseSurface = Color(0xFFF8F5F0),
    inverseOnSurface = Color(0xFF121010),
    inversePrimary = DeepGold,

    scrim = Color(0xCC000000),
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


val ColorScheme.backgroundColorCompat: Color
    @Composable get() = BackgroundColor

val ColorScheme.headlineColorCompat: Color
    @Composable get() = HeadlineColor

val ColorScheme.paragraphColorCompat: Color
    @Composable get() = ParagraphColor

val ColorScheme.buttonColorCompat: Color
    @Composable get() = ButtonColor

val ColorScheme.buttonTextColorCompat: Color
    @Composable get() = ButtonTextColor