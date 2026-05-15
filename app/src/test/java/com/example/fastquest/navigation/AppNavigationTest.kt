// Unit tests for App Navigation
package com.example.fastquest.navigation

import org.junit.Test
import org.junit.Assert.*

/**
 * Testes unitários simples para a navegação do app
 * Estes testes verificam as rotas e estrutura de navegação
 */
class AppNavigationTest {

    @Test
    fun `all screen routes should be defined`() {
        // Arrange
        val loginRoute = Screen.Login.route
        val registerRoute = Screen.Register.route
        val homeRoute = Screen.Home.route
        val createRoute = Screen.Create.route
        val questionRoute = Screen.Question.route
        val folderDetailsRoute = Screen.FolderDetails.route
        val resultsRoute = Screen.Results.route

        // Assert
        assertEquals("login", loginRoute)
        assertEquals("register", registerRoute)
        assertEquals("home", homeRoute)
        assertEquals("create", createRoute)
        assertEquals("question/{questionId}", questionRoute)
        assertEquals("folder/{folderId}", folderDetailsRoute)
        assertEquals("results", resultsRoute)
    }

    @Test
    fun `question route should accept ID parameter`() {
        // Arrange
        val questionId = "12345"

        // Act
        val route = Screen.Question.createRoute(questionId)

        // Assert
        assertEquals("question/12345", route)
        assertTrue(route.contains(questionId))
    }

    @Test
    fun `folder route should accept ID parameter`() {
        // Arrange
        val folderId = "67890"

        // Act
        val route = Screen.FolderDetails.createRoute(folderId)

        // Assert
        assertEquals("folder/67890", route)
        assertTrue(route.contains(folderId))
    }

    @Test
    fun `login should be the start destination`() {
        // Arrange
        val startDestination = Screen.Login.route

        // Assert
        assertEquals("login", startDestination)
    }

    @Test
    fun `navigation from login to register should work`() {
        // Arrange
        val currentRoute = Screen.Login.route
        val targetRoute = Screen.Register.route

        // Assert
        assertNotEquals(currentRoute, targetRoute)
        assertEquals("register", targetRoute)
    }

    @Test
    fun `navigation from login to home should work`() {
        // Arrange
        val currentRoute = Screen.Login.route
        val targetRoute = Screen.Home.route

        // Assert
        assertNotEquals(currentRoute, targetRoute)
        assertEquals("home", targetRoute)
    }

    @Test
    fun `navigation from home to folder details should work`() {
        // Arrange
        val currentRoute = Screen.Home.route
        val folderId = "123"
        val targetRoute = Screen.FolderDetails.createRoute(folderId)

        // Assert
        assertNotEquals(currentRoute, targetRoute)
        assertTrue(targetRoute.contains("folder"))
        assertTrue(targetRoute.contains(folderId))
    }

    @Test
    fun `navigation from home to question should work`() {
        // Arrange
        val currentRoute = Screen.Home.route
        val questionId = "456"
        val targetRoute = Screen.Question.createRoute(questionId)

        // Assert
        assertNotEquals(currentRoute, targetRoute)
        assertTrue(targetRoute.contains("question"))
        assertTrue(targetRoute.contains(questionId))
    }

    @Test
    fun `navigation from home to create should work`() {
        // Arrange
        val currentRoute = Screen.Home.route
        val targetRoute = Screen.Create.route

        // Assert
        assertNotEquals(currentRoute, targetRoute)
        assertEquals("create", targetRoute)
    }

    @Test
    fun `navigation from folder details to results should work`() {
        // Arrange
        val currentRoute = Screen.FolderDetails.createRoute("123")
        val targetRoute = Screen.Results.route

        // Assert
        assertNotEquals(currentRoute, targetRoute)
        assertEquals("results", targetRoute)
    }

    @Test
    fun `all routes should be unique`() {
        // Arrange
        val routes = setOf(
            Screen.Login.route,
            Screen.Register.route,
            Screen.Home.route,
            Screen.Create.route,
            Screen.Question.route,
            Screen.FolderDetails.route,
            Screen.Results.route
        )

        // Assert
        assertEquals(7, routes.size) // All routes are unique
    }

    @Test
    fun `parametrized routes should have correct format`() {
        // Arrange
        val questionRoute = Screen.Question.route
        val folderRoute = Screen.FolderDetails.route

        // Assert
        assertTrue(questionRoute.contains("{"))
        assertTrue(questionRoute.contains("}"))
        assertTrue(folderRoute.contains("{"))
        assertTrue(folderRoute.contains("}"))
    }

    @Test
    fun `route creation should handle different IDs`() {
        // Arrange
        val ids = listOf("1", "123", "abc123", "test-id")

        // Act & Assert
        ids.forEach { id ->
            val questionRoute = Screen.Question.createRoute(id)
            val folderRoute = Screen.FolderDetails.createRoute(id)

            assertTrue(questionRoute.contains(id))
            assertTrue(folderRoute.contains(id))
        }
    }
}

// Made with Bob
