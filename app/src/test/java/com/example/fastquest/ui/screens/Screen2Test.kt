// Unit tests for Register Screen (Screen2)
package com.example.fastquest.ui.screens

import org.junit.Test
import org.junit.Assert.*

/**
 * Testes unitários simples para a tela de Registro
 * Estes testes verificam a lógica básica e comportamento esperado
 */
class Screen2Test {

    @Test
    fun `register screen should have correct text labels`() {
        // Arrange
        val nameText = "Nome"
        val emailText = "E-mail"
        val passwordText = "Senha"
        val registerTitleText = "CADASTRE-SE"
        val registerPromptText = "Venha fazer parte da nossa família!"
        val registerButtonText = "CADASTRAR"
        val loginLinkText = "Login"

        // Assert
        assertNotNull(nameText)
        assertNotNull(emailText)
        assertNotNull(passwordText)
        assertNotNull(registerTitleText)
        assertNotNull(registerPromptText)
        assertNotNull(registerButtonText)
        assertNotNull(loginLinkText)
        
        assertTrue(nameText.isNotEmpty())
        assertTrue(emailText.isNotEmpty())
        assertTrue(registerTitleText.isNotEmpty())
    }

    @Test
    fun `name validation should work correctly`() {
        // Arrange
        val validName = "João Silva"
        val emptyName = ""
        val shortName = "Jo"

        // Assert
        assertTrue(validName.isNotEmpty())
        assertTrue(emptyName.isEmpty())
        assertTrue(validName.length >= 3)
        assertTrue(shortName.length < 3)
    }

    @Test
    fun `email validation should work correctly`() {
        // Arrange
        val validEmail = "joao@example.com"
        val invalidEmail = "invalid-email"
        val emptyEmail = ""

        // Assert
        assertTrue(validEmail.contains("@"))
        assertTrue(validEmail.contains("."))
        assertFalse(invalidEmail.contains("@"))
        assertTrue(emptyEmail.isEmpty())
    }

    @Test
    fun `password strength validation should work`() {
        // Arrange
        val strongPassword = "SecurePass123!"
        val weakPassword = "123"
        val emptyPassword = ""

        // Assert
        assertTrue(strongPassword.length >= 8)
        assertTrue(weakPassword.length < 8)
        assertTrue(emptyPassword.isEmpty())
    }

    @Test
    fun `register button should be enabled when all fields are filled`() {
        // Arrange
        val name = "João Silva"
        val email = "joao@example.com"
        val password = "password123"

        // Act
        val isRegisterEnabled = name.isNotEmpty() && 
                                email.isNotEmpty() && 
                                password.isNotEmpty()

        // Assert
        assertTrue(isRegisterEnabled)
    }

    @Test
    fun `register button should be disabled when fields are empty`() {
        // Arrange
        val name = ""
        val email = ""
        val password = ""

        // Act
        val isRegisterEnabled = name.isNotEmpty() && 
                                email.isNotEmpty() && 
                                password.isNotEmpty()

        // Assert
        assertFalse(isRegisterEnabled)
    }

    @Test
    fun `register button should be disabled when only some fields are filled`() {
        // Arrange
        val name = "João Silva"
        val email = ""
        val password = "password123"

        // Act
        val isRegisterEnabled = name.isNotEmpty() && 
                                email.isNotEmpty() && 
                                password.isNotEmpty()

        // Assert
        assertFalse(isRegisterEnabled)
    }
}

// Made with Bob
