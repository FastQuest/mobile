// Navigation setup connecting all screens in the FastQuest app
package com.example.fastquest.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.fastquest.ui.screens.*

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object Home : Screen("home")
    data object Create : Screen("create")
    data object Question : Screen("question/{questionId}") {
        fun createRoute(questionId: String) = "question/$questionId"
    }
    data object FolderDetails : Screen("folder/{folderId}") {
        fun createRoute(folderId: String) = "folder/$folderId"
    }
    data object Results : Screen("results")
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
        // Login screen
        composable(Screen.Login.route) {
            Screen1(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Register screen
        composable(Screen.Register.route) {
            Screen2(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onRegisterClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Home screen
        composable(Screen.Home.route) {
            HomeScreen(
                onFolderClick = { folderId ->
                    navController.navigate(Screen.FolderDetails.createRoute(folderId))
                },
                onQuestionClick = { questionId ->
                    navController.navigate(Screen.Question.createRoute(questionId))
                },
                onMenuClick = {
                    // Handle menu click - could open drawer
                },
                onFilterClick = {
                    // Handle filter click - could open filter dialog
                },
                onCreateClick = {
                    navController.navigate(Screen.Create.route)
                }
            )
        }

        // Create screen
        composable(Screen.Create.route) {
            CreateScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onMenuClick = {
                    // Handle menu click
                },
                onCreateQuestionnaireClick = {
                    // Navigate to questionnaire creation (not implemented yet)
                },
                onCreateQuestionClick = {
                    // Navigate to question creation (not implemented yet)
                }
            )
        }

        // Question screen
        composable(
            route = Screen.Question.route,
            arguments = listOf(
                navArgument("questionId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val questionId = backStackEntry.arguments?.getString("questionId") ?: ""
            QuestionScreen(
                questionNumber = "#$questionId",
                onBackClick = {
                    navController.popBackStack()
                },
                onInfoClick = {
                    // Show question info dialog
                },
                onMenuClick = {
                    // Handle menu click
                },
                onConfirmClick = {
                    // Handle answer confirmation
                }
            )
        }

        // Folder details screen
        composable(
            route = Screen.FolderDetails.route,
            arguments = listOf(
                navArgument("folderId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val folderId = backStackEntry.arguments?.getString("folderId") ?: ""
            FolderDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onMenuClick = {
                    // Handle menu click
                },
                onViewQuestionsClick = {
                    // Navigate to questions list in folder
                },
                onViewResultsClick = {
                    navController.navigate(Screen.Results.route)
                },
                onRespondClick = {
                    // Start answering questions in folder
                    navController.navigate(Screen.Question.createRoute("1"))
                }
            )
        }

        // Results screen
        composable(Screen.Results.route) {
            ResultsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onMenuClick = {
                    // Handle menu click
                }
            )
        }
    }
}

// Made with Bob
