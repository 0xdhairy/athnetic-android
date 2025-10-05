package com.dhairy.athnetic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(userInfo: UserInfo? ,onLogout: () -> Unit) {
    val scope = rememberCoroutineScope()
    println("User Info: ${userInfo?.userMetadata?.get("picture")}")
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Welcome, ${userInfo?.userMetadata?.get("name").toString().replace("\"" , "")}", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
            AsyncImage(
                model = userInfo?.userMetadata?.get("picture")?.toString()?.replace("\"", ""),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                scope.launch { onLogout() }
            }) {
                Text("Logout")
            }
        }
    }
}

