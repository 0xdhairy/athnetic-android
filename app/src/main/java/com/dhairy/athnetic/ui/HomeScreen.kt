package com.dhairy.athnetic.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    // just placeholder
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier.fillMaxSize().padding(20.dp).padding(top = 100.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text("Athnetic", style = MaterialTheme.typography.headlineMedium, fontSize = 50.sp, modifier = Modifier.padding(bottom=20.dp))
            Text("Will add later - check out Feedback Section", style = MaterialTheme.typography.bodyMedium, fontSize = 20.sp)
        }

    }
}
