package com.dhairy.athnetic.ui

import android.content.Context
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
import androidx.core.content.edit

@Serializable
data class Feedback(
    val content: String, val created_by: String
)

/**
 * Feedback screen allowing users to submit their feedback.
 * The feedback is stored in the Supabase "feedback" table.
 */

@Composable

fun FeedbackScreen(context: Context) {
    var content by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val openDialog = remember { mutableStateOf(false) }
    var openDialogTitle by remember { mutableStateOf("") }
    var openDialogText by remember { mutableStateOf("") }
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
        FeedbackAlertDialog(
            title = openDialogTitle,
            text = openDialogText,
            openDialog = openDialog
        )
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
                if (content.isBlank()) {
                    openDialog.value = true
                    openDialogText = "Feedback cannot be empty."
                    openDialogTitle = "Empty Feedback"
                    return@Button
                }
                if (content.length > 500) {
                    openDialog.value = true
                    openDialogText = "Feedback cannot exceed 500 characters."
                    openDialogTitle = "Feedback Too Long"
                    return@Button
                }
                if (content.length < 10) {
                    openDialog.value = true
                    openDialogText = "Feedback should be at least 10 characters."
                    openDialogTitle = "Feedback Too Short"
                    return@Button
                }
                scope.launch {
                    try {
                        openDialog.value = true
                        openDialogText =
                            "Feedback submitted successfully! Thank you for helping us improve Athnetic."
                        openDialogTitle = "Success"
                        val userId = Supabase.client.auth.currentUserOrNull()?.id
                            ?: throw Exception("User not logged in")

                        val feedback = Feedback(content = content, created_by = userId)
                        println("Inserting feedback: $feedback")
                        Supabase.client.from("feedback").insert(feedback)
                        message = "Feedback submitted successfully!"
                        val prefs = context.getSharedPreferences("cooldown", Context.MODE_PRIVATE)
                        prefs.edit { putLong("last_click", System.currentTimeMillis()) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        message = "Error, Contact the developer. \n ${e.message}"
                    }
                }
            }, modifier = Modifier.fillMaxWidth(),
            enabled = canClick(context)
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
    FeedbackScreen(context = androidx.compose.ui.platform.LocalContext.current)
}
fun canClick(context: Context): Boolean {
    val prefs = context.getSharedPreferences("cooldown", Context.MODE_PRIVATE)
    val lastClick = prefs.getLong("last_click", 0L)
    val elapsed = System.currentTimeMillis() - lastClick
    return elapsed >= 60_000 // 1 minute
}


@Composable
fun FeedbackAlertDialogBuilder(
    title: String,
    text: String,
    openDialog: MutableState<Boolean>,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
                onDismiss()
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(text = text)
            },
            confirmButton = {
                Button(
                    onClick = {
                        onConfirm()
                        openDialog.value = false
                    },
                )
                {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
fun FeedbackAlertDialog(
    title: String,
    text: String,
    openDialog: MutableState<Boolean>,
) {
    FeedbackAlertDialogBuilder(
        title = title,
        text = text,
        openDialog = openDialog,
        onConfirm = {},
        onDismiss = {}
    )
}
