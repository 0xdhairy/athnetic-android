package com.dhairy.athnetic.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.dhairy.athnetic.supabase
import io.github.jan.supabase.auth.auth

@Composable
fun AuthScreen(onLoggedIn: () -> Unit, signInWithGoogle: () -> Unit) {
    LaunchedEffect(Unit) {
        val session = supabase.auth.currentSessionOrNull()
        if (session != null) {
            onLoggedIn()
        }
    }

    LoginScreen(
        onLogin = { signInWithGoogle() }
    )
}

