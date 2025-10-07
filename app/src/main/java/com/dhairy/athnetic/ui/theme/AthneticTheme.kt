package com.dhairy.athnetic.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val lightColorScheme = lightColorScheme(
    primary = Color(0xFFF00000),
    background = Color.White
)
val darkColorScheme = darkColorScheme(
    primary = Color(0xFFF00000),
    onPrimary = Color.White,
)

@Composable
fun AthneticTheme(
    darkTheme: Boolean = false, // Force light theme for dev
    content: @Composable () -> Unit
) {
    val myColorScheme = if (darkTheme) darkColorScheme else lightColorScheme
    MaterialTheme(
        colorScheme = myColorScheme,
        content = content
    )

}