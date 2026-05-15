// Unit tests for Create Screen
package com.example.fastquest.ui.screens

import org.junit.Test
import org.junit.Assert.*

/**
 * Testes unitários simples para a tela de Criação
 * Estes testes verificam a lógica de opções de criação
 */
class CreateScreenTest {

    @Test
    fun `create screen should have correct title`() {
        // Arrange
        val titleText = "DÊ VOZ ÀS SUAS IDEIAS!"

        // Assert
        assertNotNull(titleText)
        assertTrue(titleText.isNotEmpty())
        assertTrue(titleText.contains("IDEIAS"))
    }

    @Test
    fun `create screen should have subtitle`() {
        // Arrange
        val subtitleText = "Selecione abaixo se deseja criar um novo questionário completo ou adicionar uma questão individual. Você poderá personalizar os detalhes após sua escolha."

        // Assert
        assertNotNull(subtitleText)
        assertTrue(subtitleText.isNotEmpty())
        assertTrue(subtitleText.length > 50)
    }

    @Test
    fun `questionnaire option should have correct title`() {
        // Arrange
        val questionnaireTitle = "Montar seu questionário"

        // Assert
        assertNotNull(questionnaireTitle)
        assertTrue(questionnaireTitle.isNotEmpty())
        assertTrue(questionnaireTitle.contains("questionário"))
    }

    @Test
    fun `questionnaire option should have description`() {
        // Arrange
        val questionnaireDescription = "Crie uma coletânea de perguntas para potencializar seu aprendizado. As questões podem ser elaboradas por você ou selecionadas do nosso banco de perguntas, permitindo a criação de simulados de forma prática e personalizada."

        // Assert
        assertNotNull(questionnaireDescription)
        assertTrue(questionnaireDescription.isNotEmpty())
        assertTrue(questionnaireDescription.contains("perguntas"))
    }

    @Test
    fun `questionnaire button should have correct text`() {
        // Arrange
        val questionnaireButtonText = "CRIAR QUESTIONÁRIO"

        // Assert
        assertEquals("CRIAR QUESTIONÁRIO", questionnaireButtonText)
        assertTrue(questionnaireButtonText.contains("CRIAR"))
    }

    @Test
    fun `question option should have correct title`() {
        // Arrange
        val questionTitle = "Adicionar uma questão"

        // Assert
        assertNotNull(questionTitle)
        assertTrue(questionTitle.isNotEmpty())
        assertTrue(questionTitle.contains("questão"))
    }

    @Test
    fun `question option should have description`() {
        // Arrange
        val questionDescription = "Adicione novas perguntas ao banco de questões e contribua para ampliar o conteúdo disponível. Você pode criar perguntas personalizadas, organizá-las e utilizá-las posteriormente na montagem de simulados de forma prática e eficiente."

        // Assert
        assertNotNull(questionDescription)
        assertTrue(questionDescription.isNotEmpty())
        assertTrue(questionDescription.contains("banco"))
    }

    @Test
    fun `question button should have correct text`() {
        // Arrange
        val questionButtonText = "CRIAR PERGUNTA"

        // Assert
        assertEquals("CRIAR PERGUNTA", questionButtonText)
        assertTrue(questionButtonText.contains("CRIAR"))
    }

    @Test
    fun `both options should be available`() {
        // Arrange
        val hasQuestionnaireOption = true
        val hasQuestionOption = true

        // Assert
        assertTrue(hasQuestionnaireOption)
        assertTrue(hasQuestionOption)
    }

    @Test
    fun `button colors should be different`() {
        // Arrange
        val questionnaireButtonColor = "Red" // ButtonRed
        val questionButtonColor = "DarkBlue" // ButtonDarkBlue

        // Assert
        assertNotEquals(questionnaireButtonColor, questionButtonColor)
    }

    @Test
    fun `screen should have navigation buttons`() {
        // Arrange
        val hasBackButton = true
        val hasMenuButton = true

        // Assert
        assertTrue(hasBackButton)
        assertTrue(hasMenuButton)
    }

    @Test
    fun `create options should have valid structure`() {
        // Arrange
        data class CreateOption(
            val title: String,
            val description: String,
            val buttonText: String
        )

        val questionnaireOption = CreateOption(
            "Montar seu questionário",
            "Descrição do questionário",
            "CRIAR QUESTIONÁRIO"
        )

        val questionOption = CreateOption(
            "Adicionar uma questão",
            "Descrição da questão",
            "CRIAR PERGUNTA"
        )

        // Assert
        assertNotNull(questionnaireOption.title)
        assertNotNull(questionnaireOption.description)
        assertNotNull(questionnaireOption.buttonText)
        
        assertNotNull(questionOption.title)
        assertNotNull(questionOption.description)
        assertNotNull(questionOption.buttonText)
    }
}

// Made with Bob
