package com.example.fastquest.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LoginGradientStart,
    secondary = RegisterGradientStart,
    tertiary = LoginGradientEnd,
    background = LoginGradientEnd,
    surface = TextFieldBackground,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = LoginGradientStart,
    secondary = RegisterGradientStart,
    tertiary = LoginGradientEnd,
    background = Color.White,
    surface = TextFieldBackground,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onBackground = Color.Black,
    onSurface = Color.Black
)

@Composable
fun FastQuestTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Made with Bob
