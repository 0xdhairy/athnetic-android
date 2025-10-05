package com.dhairy.athnetic

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.lifecycle.lifecycleScope
import com.dhairy.athnetic.ui.HomeScreen
import com.dhairy.athnetic.ui.LoginScreen
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.handleDeeplinks
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.user.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import kotlinx.coroutines.launch
import kotlin.time.ExperimentalTime
import androidx.core.content.edit
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

val supabase = createSupabaseClient(
    supabaseUrl = "https://jcxjaoftbaslfnjapkdb.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImpjeGphb2Z0YmFzbGZuamFwa2RiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTcxMzQ2MzksImV4cCI6MjA3MjcxMDYzOX0.PmExwcl_fNAZdM1wvs9jXoWHLYaHTuqZwVJemLu8uOg"
) {
    install(Auth) {
        scheme = "athnetic"
        host = "login"
        autoLoadFromStorage = true
    }
    install(Postgrest)
}

class MainActivity : ComponentActivity() {
    private fun saveSession(session: UserSession) {
        val prefs = getSharedPreferences("supabase", MODE_PRIVATE)
        prefs.edit {
            putString("accessToken", session.accessToken)
                .putString("refreshToken", session.refreshToken)
                .putString("tokenType", session.tokenType)
                .putLong("expiresIn", session.expiresIn)
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun loadSession(): UserSession? {
        val prefs = getSharedPreferences("supabase", MODE_PRIVATE)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            // 1️⃣ Load saved session if exists
            loadSession()?.let { supabase.auth.importSession(it) }

            // 2️⃣ Handle OAuth redirect if app opened via deeplink
            handleDeepLink(intent)

            // 3️⃣ Update UI
            updateUI()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        lifecycleScope.launch {
            handleDeepLink(intent)
            updateUI()
        }
    }
    private fun clearSavedSession() {
        val prefs = getSharedPreferences("supabase", MODE_PRIVATE)
        prefs.edit { clear() }
    }

    @Composable
    fun AppNavHost(
        navController: NavHostController,
        startDestination: Destination,
        modifier: Modifier = Modifier
    ) {
        NavHost(
            navController,
            startDestination = startDestination.route
        ) {
            Destination.entries.forEach { destination ->

                val session = supabase.auth.currentSessionOrNull()

                composable(destination.route) {
                    when (destination) {
                        Destination.HOME -> HomeScreen(session?.user) {
                            // Logout button clicked
                            lifecycleScope.launch {
                                supabase.auth.signOut()       // remove session from memory
                                clearSavedSession()           // remove session from storage
                                updateUI()                    // switch to LoginScreen
                            }
                        }
                        Destination.PROFILE -> ProfileScreen()
                    }
                }
            }
        }
    }
    @Composable
    fun NavigationBar(modifier: Modifier = Modifier) {
        val navController = rememberNavController()
        val startDestination = Destination.HOME
        val selectedDestination = rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

        Scaffold(
            modifier = modifier,
            bottomBar = {
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                    Destination.entries.forEachIndexed { index, destination ->
                        NavigationBarItem(
                            selected = selectedDestination.value == index,
                            onClick = {
                                navController.navigate(route = destination.route)
                                selectedDestination.value = index
                            },
                            icon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = destination.icon),
                                    contentDescription = destination.contentDescription
                                )
                            },
                            label = { Text(destination.label) }
                        )
                    }
                }
            }
        ) { contentPadding ->
            AppNavHost(navController, startDestination, modifier = Modifier.padding(contentPadding))
        }
    }



    @Composable
    fun ProfileScreen() {
        println("💝💝 Profile Screen Loaded")
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Profile Screen")
        }
    }

    // Compose UI based on current session
    private suspend fun updateUI() {
        val session = supabase.auth.currentSessionOrNull()

        println("Current session: $session , From updateUI()")
        val userInfo = if (session != null) {
            supabase.auth.retrieveUserForCurrentSession(updateSession = true)
        } else null
        setContent {

            if (session != null) {
                NavigationBar()
            } else {
                LoginScreen {
                    lifecycleScope.launch {
                        try {
                            supabase.auth.signInWith(Google)
                        } catch (e: Exception) {
                            println("Login failed: ${e.message}")
                        }
                    }
                }
            }
        }
    }
    @OptIn(ExperimentalTime::class)
    private suspend fun handleDeepLink(intent: Intent?) {
        intent?.data?.let { uri ->
            println("Deep link received: $uri")

            supabase.handleDeeplinks(intent)

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

            supabase.auth.importSession(session)
            saveSession(session)
            val userInfo = supabase.auth.retrieveUserForCurrentSession(updateSession = true).userMetadata
            println("Session set manually: ${supabase.auth.currentSessionOrNull()}")
        }
    }
}
enum class Destination(
    val route: String,
    val label: String,
    val icon: Int,
    val contentDescription: String
) {
    HOME("home", "Home", R.drawable.ic_home_black_24dp, "Songs"),
    PROFILE("profile", "Profile (TODO)", R.drawable.ic_dashboard_black_24dp, "Album"),
}

