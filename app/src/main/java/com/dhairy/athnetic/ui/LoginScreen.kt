package com.dhairy.athnetic.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.alpha

import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview


import androidx.compose.ui.unit.sp
import com.dhairy.athnetic.R

val interFontFamily = FontFamily(
    Font(R.font.inter_bold, FontWeight.Bold),
)

/**
 * Login screen with options to sign in using Google or Apple.
 * @param signInWithGoogle A function to call when the user chooses to sign in with Google.
 */
@Composable
fun LoginScreen(signInWithGoogle: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.athnetic), // Add a background image in drawable
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(550.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
                .padding(24.dp)
                .padding(top = 470.dp)

        ) {

            val showAlertMessage = remember { mutableStateOf(false) }
            if (showAlertMessage.value) {
                AlertDialog(
                    onDismissRequest = {
                        showAlertMessage.value = false
                    },
                    title = {
                        Text(text = "Unavailable")
                    },
                    text = {
                        Text("Sign in with Apple is currently unavailable.")

                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showAlertMessage.value = false
                            },
                            ) {
                            Text("OK")
                        }
                    }
                )
            }


            // --- Subtitle ---
            Text(
                text = "Sign in to Athnetic",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp),
            )

            // --- Google sign-in button ---
            Button(
                onClick = signInWithGoogle,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color.LightGray),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google), // add google icon in drawable
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign in with Google", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Apple sign-in button ---
            Button(
                onClick = {
                    showAlertMessage.value = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.apple),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(15.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Sign in with Apple", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(signInWithGoogle = {})
}