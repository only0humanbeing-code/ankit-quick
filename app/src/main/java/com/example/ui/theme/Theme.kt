package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = QuickGreen,
    onPrimary = Color.White,
    primaryContainer = QuickGreenContainer,
    onPrimaryContainer = QuickGreenOnContainer,
    secondary = QuickGold,
    onSecondary = Color.White,
    secondaryContainer = QuickAmberContainer,
    onSecondaryContainer = QuickSlate,
    tertiary = QuickBlue,
    onTertiary = Color.White,
    tertiaryContainer = QuickBlueContainer,
    onTertiaryContainer = QuickSlate,
    background = QuickBackground,
    onBackground = QuickSlate,
    surface = QuickSurface,
    onSurface = QuickSlate,
    surfaceVariant = QuickSurfaceVariant,
    onSurfaceVariant = QuickMuted,
    outline = QuickOutline,
    error = QuickRed,
    onError = Color.White,
    errorContainer = QuickRedContainer,
    onErrorContainer = QuickRed
)

private val DarkColorScheme = darkColorScheme(
    primary = QuickDarkGreen,
    onPrimary = QuickDarkBackground,
    primaryContainer = QuickDarkGreenContainer,
    onPrimaryContainer = Color(0xFFA7F3D0),
    secondary = QuickAmber,
    onSecondary = QuickSlate,
    secondaryContainer = Color(0xFF451A03),
    onSecondaryContainer = Color(0xFFFDE68A),
    tertiary = Color(0xFF60A5FA),
    onTertiary = QuickSlate,
    background = QuickDarkBackground,
    onBackground = QuickDarkText,
    surface = QuickDarkSurface,
    onSurface = QuickDarkText,
    surfaceVariant = QuickDarkSurfaceVariant,
    onSurfaceVariant = QuickDarkMuted,
    outline = Color(0xFF2E3C34),
    error = Color(0xFFF87171),
    onError = QuickSlate
)

val QuickKartShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun QuickKartTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = QuickKartTypography,
        shapes = QuickKartShapes,
        content = content
    )
}
