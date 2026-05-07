// Question screen - Displays a question with timer, text, and multiple choice options
package com.example.fastquest.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fastquest.ui.components.*
import com.example.fastquest.ui.theme.*

data class QuestionOption(
    val id: String,
    val text: String
)

@Composable
fun QuestionScreen(
    questionNumber: String = "#3536562563",
    questionText: String = "",
    options: List<QuestionOption> = emptyList(),
    onBackClick: () -> Unit = {},
    onInfoClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onPlayClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onResetClick: () -> Unit = {}
) {
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var timerText by remember { mutableStateOf("00 : 00 : 00") }
    var isPlaying by remember { mutableStateOf(false) }

    val defaultQuestionText = "Soraya integrava o contrato social de uma sociedade empresária, mas se afastou dela em 2019 e registrou sua saída perante a Junta Comercial em dezembro de 2021. Joana foi empregada da sociedade empresária em questão de abril de 2019 a setembro de 2020, tendo sido dispensada sem justa causa. Obteve vitória judicial e iniciou a execução em janeiro de 2025.\n\nNão tendo a sociedade empresária solvabilidade, requereu o direcionamento da execução contra os sócios atuais, sem êxito. Então, requereu que a execução fosse feita em relação à Soraya. Considerando esses fatos e o que prevê a CLT, assinale a afirmativa correta."

    val defaultOptions = listOf(
        QuestionOption("A", "É possível a execução de Soraya porque, entre a averbação de sua saída e o ajuizamento da ação, transcorreu prazo inferior a 2 anos."),
        QuestionOption("B", "É possível a execução de Soraya porque, entre a averbação de sua saída e o ajuizamento da ação, transcorreu prazo inferior a 2 anos."),
        QuestionOption("C", "É possível a execução de Soraya porque, entre a averbação de sua saída e o ajuizamento da ação, transcorreu prazo inferior a 2 anos."),
        QuestionOption("D", "É possível a execução de Soraya porque, entre a averbação de sua saída e o ajuizamento da ação, transcorreu prazo inferior a 2 anos.")
    )

    val displayQuestionText = questionText.ifEmpty { defaultQuestionText }
    val displayOptions = options.ifEmpty { defaultOptions }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(QuestionBackgroundRed)
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
                        .background(Color.Black, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "Questão $questionNumber",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    IconButton(
                        onClick = onInfoClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(TextFieldBackground, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Informações",
                            tint = Color.Black
                        )
                    }

                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(TextFieldBackground, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.Black
                        )
                    }
                }
            }

            // Timer section
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(28.dp),
                color = TextFieldBackground
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = timerText,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(
                            onClick = {
                                isPlaying = false
                                onPauseClick()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .background(ButtonRed, RoundedCornerShape(2.dp))
                            )
                        }

                        IconButton(
                            onClick = {
                                isPlaying = true
                                onPlayClick()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = "Play",
                                tint = Color.Black
                            )
                        }

                        IconButton(
                            onClick = {
                                timerText = "00 : 00 : 00"
                                isPlaying = false
                                onResetClick()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Question and options
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Question text
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = CardBackground
                ) {
                    Text(
                        text = displayQuestionText,
                        fontSize = 14.sp,
                        color = TextDark,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // Options
                displayOptions.forEach { option ->
                    QuestionOptionItem(
                        option = option,
                        isSelected = selectedOption == option.id,
                        onClick = { selectedOption = option.id }
                    )
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Confirm button
        Button(
            onClick = onConfirmClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .width(280.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = TextPrimary
            ),
            enabled = selectedOption != null
        ) {
            Text(
                text = "CONFIRMAR",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun QuestionOptionItem(
    option: QuestionOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier.border(3.dp, Color.Black, RoundedCornerShape(16.dp))
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(16.dp),
        color = CardBackground
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Option letter
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) Color.Black else Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = option.id,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) TextPrimary else Color.Black
                )
            }

            // Option text
            Text(
                text = option.text,
                fontSize = 13.sp,
                color = TextDark,
                lineHeight = 18.sp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuestionScreenPreview() {
    FastQuestTheme {
        QuestionScreen()
    }
}

// Made with Bob
