package com.dhairy.athnetic

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.core.content.edit
import io.github.jan.supabase.auth.user.UserSession
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

/**
 * Manages saving, loading, and clearing user sessions using SharedPreferences.
 */
class SessionManager() {
    companion object {
        fun saveSession(context: Context, session: UserSession) {
            val prefs = context.getSharedPreferences("supabase", MODE_PRIVATE)
            prefs.edit {
                putString("accessToken", session.accessToken)
                    .putString("refreshToken", session.refreshToken)
                    .putString("tokenType", session.tokenType)
                    .putLong("expiresIn", session.expiresIn)
            }
        }

        @OptIn(ExperimentalTime::class)
        fun loadSession(context: Context): UserSession? {
            val prefs = context.getSharedPreferences("supabase", MODE_PRIVATE)
            val accessToken = prefs.getString("accessToken", null) ?: return null
            val refreshToken = prefs.getString("refreshToken", null) ?: return null
            val tokenType = prefs.getString("tokenType", null) ?: return null
            val expiresIn = prefs.getLong("expiresIn", 0)

            return UserSession(
                accessToken = accessToken,
                refreshToken = refreshToken,
                providerRefreshToken = null,
                providerToken = null,
                expiresIn = expiresIn,
                tokenType = tokenType,
                user = null,
                type = "bearer",
                expiresAt = Clock.System.now() + expiresIn.seconds
            )
        }

        fun clearSavedSession(context: Context) {
            val prefs = context.getSharedPreferences("supabase", MODE_PRIVATE)
            prefs.edit { clear() }
        }
    }
}