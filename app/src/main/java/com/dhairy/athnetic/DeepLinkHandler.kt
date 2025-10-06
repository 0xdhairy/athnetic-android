package com.dhairy.athnetic

import android.content.Context
import android.content.Intent
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.handleDeeplinks
import io.github.jan.supabase.auth.user.UserSession
import kotlin.time.ExperimentalTime


/**
 * Very Important thing right here:
 *
 * Handle deep link intents for authentication.
 * Extracts tokens from the deep link and imports the session.
 * @param intent The intent containing the deep link data.
 * @param context The context used for saving the session.
 */
@OptIn(ExperimentalTime::class)
suspend fun handleDeepLink(intent: Intent?, context: Context) {
    intent?.data?.let { uri ->
        println("Deep link received: $uri")

        Supabase.client.handleDeeplinks(intent)

        val fragment = uri.fragment ?: return
        val params = fragment.split("&").associate {
            val (k, v) = it.split("=")
            k to v
        }
        val accessToken = params["access_token"] ?: return
        val refreshToken = params["refresh_token"] ?: return
        val expiresIn = params["expires_in"]?.toLongOrNull() ?: return
        val tokenType = params["token_type"] ?: return

        val session = UserSession(
            accessToken = accessToken,
            refreshToken = refreshToken,
            expiresIn = expiresIn,
            tokenType = tokenType,
            user = null
        )

        Supabase.client.auth.importSession(session)
        SessionManager.saveSession(context, session)
        val userInfo =
            Supabase.client.auth.retrieveUserForCurrentSession(updateSession = true).userMetadata
        println("Session set manually: ${Supabase.client.auth.currentSessionOrNull()}")
    }
}