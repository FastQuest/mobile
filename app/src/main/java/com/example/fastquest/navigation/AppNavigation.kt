// Navigation setup connecting all screens in the FastQuest app
package com.example.fastquest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fastquest.ui.screens.Screen1
import com.example.fastquest.ui.screens.Screen2

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            Screen1(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginClick = {
                    // Handle login action - no business logic needed for now
                }
            )
        }

        composable(Screen.Register.route) {
            Screen2(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterClick = {
                    // Handle register action - no business logic needed for now
                }
            )
        }
    }
}

// Made with Bob
