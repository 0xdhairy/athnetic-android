package com.dhairy.athnetic.ui.navigation

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dhairy.athnetic.Destination

/**
 * Composable function that sets up a navigation bar with a scaffold.
 * It uses a NavHostController to manage navigation between different destinations.
 * The navigation bar displays items for each destination defined in the Destination enum.
 */
@Composable
fun NavigationBar(
    modifier: Modifier = Modifier,
    appNavHost: @Composable (NavHostController, Destination, Modifier) -> Unit
) {
    val navController = rememberNavController()
    val startDestination = Destination.HOME
    val selectedDestination = rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier, bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Destination.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(selected = selectedDestination.intValue == index, onClick = {
                        navController.navigate(route = destination.route)
                        selectedDestination.intValue = index
                    }, icon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = destination.icon),
                            contentDescription = destination.contentDescription
                        )
                    }, label = { Text(destination.label) })
                }
            }
        }) { contentPadding ->
        appNavHost(navController, startDestination, Modifier.padding(contentPadding))
    }
}

