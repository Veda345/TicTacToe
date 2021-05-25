package com.veda.tictactoe.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(primary = LightGray, primaryVariant = Gray, secondary = LightGray, background = Background)

//private val LightColorPalette = lightColors(primary = LightGray, primaryVariant = Gray, secondary = LightGray, background = Background)

@Composable
fun TicTacToeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
//     Dark is cute.
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }

    MaterialTheme(colors = DarkColorPalette, typography = Typography, shapes = Shapes, content = content)
}