package hu.bme.aut.android.movies.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.ViewCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF222831),
    onPrimary = Color(0xFFeeeeee),
    secondary = Color(0xFF424851),
    onSecondary = Color(0xFF323232),
    tertiary = Color(0xFF00b5bd),
    onTertiary = Color(0xFF323232),
    background = Color(0xFFecf0f1),
    onBackground = Color(0xFF323232),
    surface = Color(0xFFecf0f1),
    onSurface = Color(0xFF323232),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF3c75a5),
    onPrimary = Color(0xFFeeeeee),
    secondary = Color(0xFFb4e1ff),
    onSecondary = Color(0xFF323232),
    tertiary = Color(0xFFffebba),
    onTertiary = Color(0xFF323232),
    background = Color(0xFFecf0f1),
    onBackground = Color(0xFF323232),
    surface = Color(0xFFecf0f1),
    onSurface = Color(0xFF323232),

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MovieTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            (view.context as Activity).window.statusBarColor = colorScheme.primary.toArgb()
            ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
