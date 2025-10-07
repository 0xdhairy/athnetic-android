package com.dhairy.athnetic

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dhairy.athnetic.ui.FeedbackScreen
import com.dhairy.athnetic.ui.theme.AthneticTheme
import com.dhairy.athnetic.ui.HomeScreen
import com.dhairy.athnetic.ui.LoginScreen
import com.dhairy.athnetic.ui.ProfileScreen
import com.dhairy.athnetic.ui.navigation.NavigationBar
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

/**
 * Main activity of the app.
 * Handles authentication state and navigation between screens.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            sessionRefresher()
            handleDeepLink(intent, this@MainActivity)
            updateUI()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        lifecycleScope.launch {
            handleDeepLink(intent, this@MainActivity)
            updateUI()
        }
    }

    /** Refresh session if a valid refresh token is available.
     * If refresh fails or no token is found, clear the saved session.
     */
    private suspend fun sessionRefresher() {
        val saved = SessionManager.loadSession(this@MainActivity)
        if (saved != null) {
            val saved = SessionManager.loadSession(this@MainActivity)!!

            Supabase.client.auth.importSession(saved)
            val current = Supabase.client.auth.currentSessionOrNull()
            if (current?.refreshToken != null) {
                val refreshed = withTimeoutOrNull(15000) { // 15 s
                    Supabase.client.auth.refreshSession(current.refreshToken)
                }
                if (refreshed != null) {
                    SessionManager.saveSession(this@MainActivity, refreshed)
                    println("✅ Session refreshed successfully.")
                } else {
                    println("⚠️ Refresh timed out or failed, clearing session.")
                    SessionManager.clearSavedSession(this@MainActivity)
                }
            } else {
                println("⚠️ No refresh token found.")
                SessionManager.clearSavedSession(this@MainActivity)
            }
        }
    }

    /** Navigation host for the app.
     * Defines navigation routes and associated composable screens.
     * @param navController Navigation controller to manage navigation actions.
     * @param startDestination The initial destination to display.
     * @param modifier Optional modifier for styling.
     */
    @Composable
    fun AppNavHost(
        navController: NavHostController,
        startDestination: Destination,
        modifier: Modifier = Modifier
    ) {
        NavHost(
            navController, startDestination = startDestination.route
        ) {
            Destination.entries.forEach { destination ->

                val session = Supabase.client.auth.currentSessionOrNull()

                composable(destination.route) {

                    when (destination) {

                        Destination.HOME -> HomeScreen()

                        Destination.PROFILE -> ProfileScreen(session?.user) {
                            // Logout button clicked
                            lifecycleScope.launch {
                                Supabase.client.auth.signOut()
                                // remove session from memory
                                SessionManager.clearSavedSession(this@MainActivity)           // remove session from storage
                                updateUI()                    // switch to LoginScreen
                            }
                        }

                        Destination.FEEDBACK -> FeedbackScreen(this@MainActivity)
                    }
                }
            }
        }
    }

    /**
     * Update the UI based on the current authentication state.
     * If a valid session exists, show the main app navigation or else show the login screen.
     */
    private fun updateUI() {
        val session = Supabase.client.auth.currentSessionOrNull()
        println("Current session: $session , From updateUI()")
        setContent {
            AthneticTheme {
                if (session != null) {
                    NavigationBar(appNavHost = { navController, startDestination, modifier ->
                        AppNavHost(
                            navController, startDestination, modifier = modifier
                        )
                    })

                } else {
                    AthneticTheme {
                        LoginScreen {
                            lifecycleScope.launch {
                                try {
                                    Supabase.client.auth.signInWith(Google)
                                } catch (e: Exception) {
                                    println("Login failed: ${e.message}")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



