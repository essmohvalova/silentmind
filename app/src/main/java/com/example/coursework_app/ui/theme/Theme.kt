package com.example.coursework_app.ui.theme


import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Светлая цветовая схема
private val LightColorScheme = lightColorScheme(
    // Основные цвета
    primary = blueAccent,
    onPrimary = White,
    primaryContainer = blueAccentLight,
    onPrimaryContainer = blueAccentDarker,

    secondary = goldAccent,
    onSecondary = White,
    secondaryContainer = goldAccentLight,
    onSecondaryContainer = goldAccentDarker,

    tertiary = greyContainer,
    onTertiary = White,
    tertiaryContainer = greyContainerLighter,
    onTertiaryContainer = greyContainerarker,

    // Фоновые цвета
    background = background,
    onBackground = TextPrimary,

    surface = backgroundLight,
    onSurface = TextPrimary,

    surfaceVariant = backgroundLighter,
    onSurfaceVariant = TextSecondary,

    // Обратная связь
    error = Color(0xFFBA1A1A),
    onError = White,
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),

    // Дополнительные цвета
    inverseSurface = DarkGray,
    inverseOnSurface = OffWhite,
    inversePrimary = blueAccentLight,

    outline = MediumGray,
    outlineVariant = LightGray,

    scrim = Black.copy(alpha = 0.4f)
)

// Темная цветовая схема
private val DarkColorScheme = darkColorScheme(
    // Основные цвета (адаптированы для темной темы)
    primary = blueAccentLight,
    onPrimary = blueAccentDarker,
    primaryContainer = blueAccentDark,
    onPrimaryContainer = blueAccentLight,

    secondary = goldAccentLight,
    onSecondary = goldAccentDarker,
    secondaryContainer = goldAccentDark,
    onSecondaryContainer = goldAccentLight,

    tertiary = greyContainerLighter,
    onTertiary = greyContainerarker,
    tertiaryContainer = greyContainerDark,
    onTertiaryContainer = greyContainerLighter,

    // Фоновые цвета (темные версии)
    background = DarkGray,
    onBackground = OffWhite,

    surface = Color(0xFF2A2A2A),
    onSurface = OffWhite,

    surfaceVariant = Color(0xFF3A3A3A),
    onSurfaceVariant = LightGray,

    // Обратная связь
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),

    // Дополнительные цвета
    inverseSurface = backgroundLight,
    inverseOnSurface = DarkGray,
    inversePrimary = blueAccent,

    outline = LightGray,
    outlineVariant = MediumGray,

    scrim = Black.copy(alpha = 0.6f)
)

@Composable
fun CourseworkAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,  // Можно включить для динамических цветов Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,  // Убедитесь, что у вас есть файл Typography.kt
        content = content
    )
}
