package com.dhairy.athnetic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.alpha

import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview


import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(onLogin: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier

            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFF5DFDF)
                    )
                )
            )
            .fillMaxSize().paint(
                painter = androidx.compose.ui.res.painterResource(id = com.dhairy.athnetic.R.drawable.bg),
                contentScale = ContentScale.FillBounds,
                alpha = 0.2f
            ).alpha(1f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .wrapContentHeight()
        ) {
            Text(
                buildAnnotatedString {

                    val RainbowColors = listOf(
                        Color(0xFFFF0000),
                        Color(0xFFFF7F00),
                    )
                    withStyle(
                        SpanStyle(
                            brush = Brush.linearGradient(
                                colors = RainbowColors
                            ),
                            fontSize = 80.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Athnetic")

                    }

                }
            )
            Spacer(modifier = Modifier.height(8.dp))


// Single Sign in with Google button
            ElevatedButton(
                onClick = onLogin,
                shape = RoundedCornerShape(8.dp),

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Sign in with Google",
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Start,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {

    LoginScreen(onLogin = {})

}