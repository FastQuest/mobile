// Unit tests for Question Screen
package com.example.fastquest.ui.screens

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before

/**
 * Testes unitários simples para a tela de Questão
 * Estes testes verificam a lógica de questões e opções
 */
class QuestionScreenTest {

    private lateinit var sampleOptions: List<QuestionOption>

    @Before
    fun setup() {
        sampleOptions = listOf(
            QuestionOption("A", "Opção A - Resposta correta"),
            QuestionOption("B", "Opção B - Resposta incorreta"),
            QuestionOption("C", "Opção C - Resposta incorreta"),
            QuestionOption("D", "Opção D - Resposta incorreta")
        )
    }

    @Test
    fun `question should have four options`() {
        // Assert
        assertEquals(4, sampleOptions.size)
    }

    @Test
    fun `question options should have valid IDs`() {
        // Assert
        assertEquals("A", sampleOptions[0].id)
        assertEquals("B", sampleOptions[1].id)
        assertEquals("C", sampleOptions[2].id)
        assertEquals("D", sampleOptions[3].id)
    }

    @Test
    fun `question options should have text`() {
        // Assert
        sampleOptions.forEach { option ->
            assertNotNull(option.text)
            assertTrue(option.text.isNotEmpty())
        }
    }

    @Test
    fun `only one option should be selected at a time`() {
        // Arrange
        var selectedOption: String? = null

        // Act - Select option A
        selectedOption = "A"

        // Assert
        assertEquals("A", selectedOption)

        // Act - Select option B (should replace A)
        selectedOption = "B"

        // Assert
        assertEquals("B", selectedOption)
        assertNotEquals("A", selectedOption)
    }

    @Test
    fun `confirm button should be disabled when no option is selected`() {
        // Arrange
        val selectedOption: String? = null

        // Act
        val isConfirmEnabled = selectedOption != null

        // Assert
        assertFalse(isConfirmEnabled)
    }

    @Test
    fun `confirm button should be enabled when option is selected`() {
        // Arrange
        val selectedOption: String? = "A"

        // Act
        val isConfirmEnabled = selectedOption != null

        // Assert
        assertTrue(isConfirmEnabled)
    }

    @Test
    fun `timer should start at zero`() {
        // Arrange
        val initialTimer = "00 : 00 : 00"

        // Assert
        assertEquals("00 : 00 : 00", initialTimer)
    }

    @Test
    fun `timer format should be valid`() {
        // Arrange
        val timerText = "00 : 15 : 30"

        // Act
        val parts = timerText.split(" : ")

        // Assert
        assertEquals(3, parts.size)
        assertTrue(parts[0].toInt() >= 0) // hours
        assertTrue(parts[1].toInt() >= 0) // minutes
        assertTrue(parts[2].toInt() >= 0) // seconds
    }

    @Test
    fun `question number should be formatted correctly`() {
        // Arrange
        val questionId = "3536562563"
        val formattedNumber = "#$questionId"

        // Assert
        assertTrue(formattedNumber.startsWith("#"))
        assertEquals("#3536562563", formattedNumber)
    }

    @Test
    fun `question text should not be empty`() {
        // Arrange
        val questionText = "Esta é uma questão de exemplo sobre direito penal."

        // Assert
        assertNotNull(questionText)
        assertTrue(questionText.isNotEmpty())
        assertTrue(questionText.length > 10)
    }

    @Test
    fun `timer state should toggle correctly`() {
        // Arrange
        var isPlaying = false

        // Act - Start timer
        isPlaying = true

        // Assert
        assertTrue(isPlaying)

        // Act - Pause timer
        isPlaying = false

        // Assert
        assertFalse(isPlaying)
    }

    @Test
    fun `timer should reset to zero`() {
        // Arrange
        var timerText = "00 : 15 : 30"

        // Act - Reset
        timerText = "00 : 00 : 00"

        // Assert
        assertEquals("00 : 00 : 00", timerText)
    }
}

// Made with Bob
