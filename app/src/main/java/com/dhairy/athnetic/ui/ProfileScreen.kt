package com.dhairy.athnetic.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.launch

/**
 * Profile screen displaying user information and logout option.
 * @param userInfo The user information to display.
 * @param onLogout A suspend function to call when the user chooses to log out.
 */
@Composable
fun ProfileScreen(userInfo: UserInfo?, onLogout: suspend () -> Unit) {
    val scope = rememberCoroutineScope()

    println("User Info: ${userInfo?.userMetadata?.get("picture")}")
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier.fillMaxSize().padding(15.dp).padding(top= 150.dp)
    ) {
        Column {

            Text(
                "Welcome, ${userInfo?.userMetadata?.get("name").toString().replace("\"", "")}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            AsyncImage(
                model = userInfo?.userMetadata?.get("picture")?.toString()?.replace("\"", ""),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(120.dp)
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            val openDialog = remember { mutableStateOf(false) }
            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Logout")
                    },
                    text = {
                        Text("Are you sure you want to logout?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                scope.launch {
                                    onLogout()
                                }
                                openDialog.value = false
                            },
                        )
                        {
                            Text("Yes")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),


                            )
                        {
                            Text("No", color = Color.Black)
                        }
                    }
                )
            }
            Button(onClick = {
                openDialog.value = true
            }) {
                Text("Logout")
            }
            Text("I will do the UI Later")
        }
    }
}

