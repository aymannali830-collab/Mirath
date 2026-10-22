package com.example.mirath.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = TerracottaPrimary,
    onPrimary = OnTerracottaPrimary,
    primaryContainer = TerracottaContainer,
    onPrimaryContainer = OnTerracottaContainer,
    secondary = TerracottaLight,
    onSecondary = Color.White,
    secondaryContainer = IvorySurfaceContainer,
    onSecondaryContainer = OnIvorySurface,
    tertiary = GoldAccent,
    onTertiary = OnGold,
    tertiaryContainer = GoldContainer,
    onTertiaryContainer = Color(0xFFFFEEBC),
    surface = IvorySurface,
    onSurface = OnIvorySurface,
    surfaceVariant = IvorySurfaceContainer,
    onSurfaceVariant = OnIvorySurfaceVariant,
    surfaceContainerLow = IvorySurfaceLow,
    surfaceContainer = IvorySurfaceContainer,
    surfaceContainerHigh = IvorySurfaceHigh,
    surfaceContainerHighest = IvorySurfaceHighest,
    outline = OutlineColor,
    outlineVariant = OutlineVariantColor,
    background = IvorySurface,
    onBackground = OnIvorySurface
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFE28466),
    onPrimary = Color(0xFF380D00),
    primaryContainer = TerracottaPrimary,
    onPrimaryContainer = Color(0xFFFFDBD1),
    secondary = Color(0xFFD8C2B6),
    onSecondary = Color(0xFF3B2D24),
    secondaryContainer = Color(0xFF4A3A31),
    onSecondaryContainer = Color(0xFFF5DED2),
    tertiary = GoldAccent,
    onTertiary = OnGold,
    tertiaryContainer = GoldContainer,
    onTertiaryContainer = Color(0xFFFFEEBC),
    surface = DarkSurface,
    onSurface = OnDarkSurface,
    surfaceVariant = DarkSurfaceContainer,
    onSurfaceVariant = OnDarkSurfaceVariant,
    surfaceContainerLow = Color(0xFF1B1410),
    surfaceContainer = DarkSurface,
    surfaceContainerHigh = Color(0xFF3E312B),
    surfaceContainerHighest = Color(0xFF4A3C36),
    outline = Color(0xFF9E8C83),
    outlineVariant = Color(0xFF55443D),
    background = DarkBackground,
    onBackground = OnDarkSurface
)

@Composable
fun MirathTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}
