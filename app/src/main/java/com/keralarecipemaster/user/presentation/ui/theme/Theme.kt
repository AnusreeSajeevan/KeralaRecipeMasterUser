package com.keralarecipemaster.user.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import com.keralarecipemaster.user.prefsstore.AuthenticationState

private val DarkColorPaletteUser = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = BritishAirways
)

private val LightColorPaletteUser = lightColors(
    primary = Bahia,
    primaryVariant = Purple700,
    secondary = BritishAirways

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

private val DarkColorPaletteOwner = darkColors(
    primary = Purple2001,
    primaryVariant = Purple7001,
    secondary = BritishAirways1
)

private val LightColorPaletteOwner = lightColors(
    primary = Bahia1,
    primaryVariant = Purple7001,
    secondary = BritishAirways1
)

@Composable
fun KeralaRecipeMasterUserTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
    authenticationState: AuthenticationState
) {
    val colors = if (authenticationState == AuthenticationState.AUTHENTICATED_RESTAURANT_OWNER) {
        if (darkTheme) {
            DarkColorPaletteOwner
        } else {
            LightColorPaletteOwner
        }
    } else {
        if (darkTheme) {
            DarkColorPaletteUser
        } else {
            LightColorPaletteUser
        }
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
