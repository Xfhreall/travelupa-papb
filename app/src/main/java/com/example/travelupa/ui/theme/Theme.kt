package com.example.travelupa.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
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
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat

// Light color scheme with travel-themed colors
private val LightColorScheme = lightColorScheme(
    // Primary colors
    primary = TravelTeal,
    onPrimary = NeutralWhite,
    primaryContainer = TravelTealContainer,
    onPrimaryContainer = OnTravelTealContainer,
    
    // Secondary colors
    secondary = WarmOrange,
    onSecondary = NeutralWhite,
    secondaryContainer = WarmOrangeContainer,
    onSecondaryContainer = OnWarmOrangeContainer,
    
    // Tertiary colors
    tertiary = SkyBlue,
    onTertiary = NeutralWhite,
    tertiaryContainer = SkyBlueContainer,
    onTertiaryContainer = OnSkyBlueContainer,
    
    // Background & Surface
    background = BackgroundLight,
    onBackground = NeutralBlack,
    surface = SurfaceLight,
    onSurface = NeutralBlack,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = DarkGray,
    
    // Error colors
    error = ErrorRed,
    onError = NeutralWhite,
    errorContainer = ErrorRedContainer,
    onErrorContainer = Color(0xFF7F1D1D),
    
    // Outline
    outline = MediumGray,
    outlineVariant = LightGray
)

// Dark color scheme with travel-themed colors
private val DarkColorScheme = darkColorScheme(
    // Primary colors
    primary = TravelTealLight,
    onPrimary = OnTravelTealContainer,
    primaryContainer = TravelTealDark,
    onPrimaryContainer = TravelTealContainer,
    
    // Secondary colors
    secondary = WarmOrangeLight,
    onSecondary = OnWarmOrangeContainer,
    secondaryContainer = WarmOrangeDark,
    onSecondaryContainer = WarmOrangeContainer,
    
    // Tertiary colors
    tertiary = SkyBlueLight,
    onTertiary = OnSkyBlueContainer,
    tertiaryContainer = SkyBlueDark,
    onTertiaryContainer = SkyBlueContainer,
    
    // Background & Surface
    background = BackgroundDark,
    onBackground = NeutralOffWhite,
    surface = SurfaceDark,
    onSurface = NeutralOffWhite,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = LightGray,
    
    // Error colors
    error = Color(0xFFFCA5A5),
    onError = Color(0xFF7F1D1D),
    errorContainer = Color(0xFF991B1B),
    onErrorContainer = ErrorRedContainer,
    
    // Outline
    outline = DarkGray,
    outlineVariant = CharcoalGray
)

// Shapes for Material 3 components
val TravelupaShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun TravelupaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to use our travel theme
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

    // Update status bar color
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = TravelupaShapes,
        content = content
    )
}