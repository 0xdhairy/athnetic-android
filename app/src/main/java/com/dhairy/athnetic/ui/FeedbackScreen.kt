package com.dhairy.athnetic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhairy.athnetic.Supabase
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data class Feedback(
    val content: String, val created_by: String
)

@Composable
fun FeedbackScreen() {
    var content by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val gradientColors = listOf(
        MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary
    )

    Column(
        modifier = Modifier
            .padding(30.dp)
            .padding(top = 150.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = buildAnnotatedString {
                append("Hey! We value your")
                withStyle(
                    SpanStyle(
                        brush = Brush.linearGradient(
                            colors = gradientColors
                        )
                    )
                ) {
                    append(" feedback.")
                }


            },
            fontSize = 40.sp,
            softWrap = true,
            style = TextStyle(
                lineHeight = 40.sp
            ),
        )
        Text(
            text = "Please share your thoughts below:",
            fontSize = 20.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Enter feedback") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                scope.launch {
                    try {
                        val userId = Supabase.client.auth.currentUserOrNull()?.id
                            ?: throw Exception("User not logged in")

                        val feedback = Feedback(content = content, created_by = userId)
                        println("Inserting feedback: $feedback")

                        val inserted = Supabase.client.from("feedback").insert(feedback)

                        println("Insert result: $inserted")
                        message = "Feedback submitted successfully!"
                    } catch (e: Exception) {
                        e.printStackTrace()
                        message = "Error, Contact the developer. \n ${e.message}"
                    }
                }
            }, modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }

        if (message.isNotEmpty()) {
            Text(text = message)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedbackScreenPreview() {
    FeedbackScreen()
}
