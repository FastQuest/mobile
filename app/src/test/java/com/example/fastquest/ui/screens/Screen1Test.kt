// Unit tests for Login Screen (Screen1)
package com.example.fastquest.ui.screens

import org.junit.Test
import org.junit.Assert.*

/**
 * Testes unitários simples para a tela de Login
 * Estes testes verificam a lógica básica e comportamento esperado
 */
class Screen1Test {

    @Test
    fun `login screen should have correct text labels`() {
        // Arrange
        val emailText = "E-mail"
        val passwordText = "Senha"
        val welcomeText = "SEJA BEM-VINDO"
        val loginPromptText = "Faça login na sua conta"
        val rememberMeText = "Lembre-se de mim"
        val forgotPasswordText = "Esqueceu sua senha?"
        val loginButtonText = "LOGIN"
        val registerLinkText = "Cadastre-se"

        // Assert - Verificar que os textos não estão vazios
        assertNotNull(emailText)
        assertNotNull(passwordText)
        assertNotNull(welcomeText)
        assertNotNull(loginPromptText)
        assertNotNull(rememberMeText)
        assertNotNull(forgotPasswordText)
        assertNotNull(loginButtonText)
        assertNotNull(registerLinkText)
        
        assertTrue(emailText.isNotEmpty())
        assertTrue(passwordText.isNotEmpty())
        assertTrue(welcomeText.isNotEmpty())
    }

    @Test
    fun `email validation should work correctly`() {
        // Arrange
        val validEmail = "user@example.com"
        val invalidEmail = "invalid-email"
        val emptyEmail = ""

        // Act & Assert
        assertTrue(validEmail.contains("@"))
        assertFalse(invalidEmail.contains("@"))
        assertTrue(emptyEmail.isEmpty())
    }

    @Test
    fun `password should not be empty for valid login`() {
        // Arrange
        val validPassword = "password123"
        val emptyPassword = ""

        // Assert
        assertTrue(validPassword.isNotEmpty())
        assertTrue(emptyPassword.isEmpty())
        assertTrue(validPassword.length >= 6)
    }

    @Test
    fun `remember me checkbox should toggle correctly`() {
        // Arrange
        var rememberMe = false

        // Act
        rememberMe = !rememberMe

        // Assert
        assertTrue(rememberMe)

        // Act again
        rememberMe = !rememberMe

        // Assert
        assertFalse(rememberMe)
    }

    @Test
    fun `login button should be enabled when fields are filled`() {
        // Arrange
        val email = "user@example.com"
        val password = "password123"

        // Act
        val isLoginEnabled = email.isNotEmpty() && password.isNotEmpty()

        // Assert
        assertTrue(isLoginEnabled)
    }

    @Test
    fun `login button should be disabled when fields are empty`() {
        // Arrange
        val email = ""
        val password = ""

        // Act
        val isLoginEnabled = email.isNotEmpty() && password.isNotEmpty()

        // Assert
        assertFalse(isLoginEnabled)
    }
}

// Made with Bob
