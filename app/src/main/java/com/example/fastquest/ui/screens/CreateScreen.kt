// Create screen - Choice between creating a questionnaire or individual question
package com.example.fastquest.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fastquest.ui.components.*
import com.example.fastquest.ui.theme.*

@Composable
fun CreateScreen(
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onCreateQuestionnaireClick: () -> Unit = {},
    onCreateQuestionClick: () -> Unit = {}
) {
    val titleText = "DÊ VOZ ÀS SUAS IDEIAS!"
    val subtitleText = "Selecione abaixo se deseja criar um novo questionário completo ou adicionar uma questão individual. Você poderá personalizar os detalhes após sua escolha."
    val questionnaireTitle = "Montar seu questionário"
    val questionnaireDescription = "Crie uma coletânea de perguntas para potencializar seu aprendizado. As questões podem ser elaboradas por você ou selecionadas do nosso banco de perguntas, permitindo a criação de simulados de forma prática e personalizada."
    val questionnaireButtonText = "CRIAR QUESTIONÁRIO"
    val questionTitle = "Adicionar uma questão"
    val questionDescription = "Adicione novas perguntas ao banco de questões e contribua para ampliar o conteúdo disponível. Você pode criar perguntas personalizadas, organizá-las e utilizá-las posteriormente na montagem de simulados de forma prática e eficiente."
    val questionButtonText = "CRIAR PERGUNTA"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreateBackgroundOrange)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black, RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = TextPrimary
                    )
                }

                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier
                        .size(40.dp)
                        .background(TextFieldBackground, RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.Black
                    )
                }
            }

            // Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Logo
                FastQuestLogo()

                Spacer(modifier = Modifier.height(8.dp))

                // Title
                Text(
                    text = titleText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )

                // Subtitle
                Text(
                    text = subtitleText,
                    fontSize = 14.sp,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Questionnaire option card
                CreateOptionCard(
                    title = questionnaireTitle,
                    description = questionnaireDescription,
                    buttonText = questionnaireButtonText,
                    buttonColor = ButtonRed,
                    onButtonClick = onCreateQuestionnaireClick
                )

                // Question option card
                CreateOptionCard(
                    title = questionTitle,
                    description = questionDescription,
                    buttonText = questionButtonText,
                    buttonColor = ButtonDarkBlue,
                    onButtonClick = onCreateQuestionClick
                )

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CreateOptionCard(
    title: String,
    description: String,
    buttonText: String,
    buttonColor: Color,
    onButtonClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = CardBackground
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Text(
                text = description,
                fontSize = 13.sp,
                color = Color.Gray,
                lineHeight = 18.sp
            )

            Button(
                onClick = onButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonColor,
                    contentColor = TextPrimary
                )
            ) {
                Text(
                    text = buttonText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateScreenPreview() {
    FastQuestTheme {
        CreateScreen()
    }
}

// Made with Bob
