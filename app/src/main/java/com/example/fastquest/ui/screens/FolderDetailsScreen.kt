// Folder details screen - Displays folder information, statistics, and action buttons
package com.example.fastquest.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun FolderDetailsScreen(
    folderTitle: String = "OAB - Questões de Direito Administrativo",
    creator: String = "Joãozinho123",
    source: String = "OAB",
    date: String = "2024",
    discipline: String = "Penal",
    questionCount: Int = 80,
    description: String = "",
    score: Int = 45,
    totalScore: Int = 80,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onViewQuestionsClick: () -> Unit = {},
    onViewResultsClick: () -> Unit = {},
    onRespondClick: () -> Unit = {}
) {
    val defaultDescription = "Esta lista reúne uma série de questões fundamentais e aprofundadas sobre o Direito Criminal, abrangendo desde os conceitos básicos até os temas mais complexos da legislação penal brasileira. As perguntas foram elaboradas com o objetivo de proporcionar uma compreensão sólida e progressiva da matéria, atendendo tanto aos estudantes em fase inicial quanto àqueles que já possuem certo domínio do conteúdo jurídico.\n\nOs tópicos abordados incluem os princípios do Direito Penal, a classificação e os elementos dos crimes, as causas de exclusão de ilicitude e culpabilidade."

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FolderBackgroundOrange)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                    text = folderTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
                )

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

            // Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Folder info card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = CardBackground
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        InfoRow("Criador:", creator)
                        InfoRow("Fonte:", source)
                        InfoRow("Data:", date)
                        InfoRow("Disciplina:", discipline)
                        InfoRow("Número de questões:", "$questionCount questões")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = description.ifEmpty { defaultDescription },
                            fontSize = 13.sp,
                            color = Color.Gray,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = onViewQuestionsClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonDarkBlue,
                                contentColor = TextPrimary
                            )
                        ) {
                            Text(
                                text = "VER QUESTÕES",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                // Score card
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
                            text = "Você acertou:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Text(
                            text = "$score/$totalScore",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Text(
                            text = "Aqui vai aparecer um resumo do seu desempenho e onde você poderá explorar os detalhes de cada questão para entender melhor seus acertos e pontos a melhorar.",
                            fontSize = 13.sp,
                            color = Color.Gray,
                            lineHeight = 18.sp
                        )

                        Button(
                            onClick = onViewResultsClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonRed,
                                contentColor = TextPrimary
                            )
                        ) {
                            Text(
                                text = "VER RESULTADOS",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        // Respond button
        Button(
            onClick = onRespondClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .width(280.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = TextPrimary
            )
        ) {
            Text(
                text = "RESPONDER",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Text(
            text = value,
            fontSize = 13.sp,
            color = TextDark
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FolderDetailsScreenPreview() {
    FastQuestTheme {
        FolderDetailsScreen()
    }
}

// Made with Bob
