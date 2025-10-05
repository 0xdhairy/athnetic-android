package com.dhairy.athnetic.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// -- Simple theme (you can replace with your project's theme) --
@Composable
fun AthneticTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF0A84FF),
            secondary = Color(0xFF5856D6),
            background = Color(0xFFF6F7FB)
        )
    ) {
        content()
    }
}