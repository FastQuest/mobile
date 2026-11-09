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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fastquest.FastQuestApplication
import com.example.fastquest.data.model.response.QuestionSet
import com.example.fastquest.ui.state.FolderDetailsUiState
import com.example.fastquest.ui.components.*
import com.example.fastquest.ui.theme.*
import com.example.fastquest.viewmodel.FolderDetailsViewModel

@Composable
fun FolderDetailsScreen(
    folderId: String? = null,
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
    onViewQuestionsClick: (String) -> Unit = {},
    onViewResultsClick: () -> Unit = {},
    onRespondClick: (String) -> Unit = {}
) {
    val defaultDescription = "Esta lista reúne uma série de questões fundamentais e aprofundadas sobre o Direito Criminal, abrangendo desde os conceitos básicos até os temas mais complexos da legislação penal brasileira. As perguntas foram elaboradas com o objetivo de proporcionar uma compreensão sólida e progressiva da matéria, atendendo tanto aos estudantes em fase inicial quanto àqueles que já possuem certo domínio do conteúdo jurídico.\n\nOs tópicos abordados incluem os princípios do Direito Penal, a classificação e os elementos dos crimes, as causas de exclusão de ilicitude e culpabilidade."

        val folderViewModel = if (folderId == null) {
            null
        } else {
            val container = (LocalContext.current.applicationContext as FastQuestApplication).container
            viewModel<FolderDetailsViewModel>(key = folderId, factory = container.folderDetailsViewModelFactory)
        }
        val folderState by if (folderViewModel == null) {
            remember { mutableStateOf(FolderDetailsUiState()) }
        } else {
            folderViewModel.state.collectAsStateWithLifecycle()
        }

        LaunchedEffect(folderId) {
            if (folderId != null) folderViewModel?.loadFolder(folderId)
        }

        if (folderId != null && folderState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return
        }

        if (folderId != null && folderState.questionSet == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(folderState.error ?: "Lista não encontrada")
                    Button(onClick = { folderViewModel?.loadFolder(folderId) }) {
                        Text("Tentar novamente")
                    }
                }
            }
            return
        }

        val questionSet: QuestionSet? = folderState.questionSet
        val firstQuestion = questionSet?.questions?.firstOrNull()
        val displayTitle = questionSet?.name ?: folderTitle
        val displayCreator = questionSet?.user?.name ?: if (questionSet == null) creator else "Não informado"
        val displaySource = firstQuestion?.source?.name ?: if (questionSet == null) source else "Não informado"
        val displayDate = firstQuestion?.source?.year?.toString()
            ?: questionSet?.createdAt?.take(4)
            ?: date
        val displayDiscipline = firstQuestion?.subject?.name
            ?: if (questionSet == null) discipline else "Não informada"
        val displayQuestionCount = questionSet?.questions?.size ?: questionCount
        val displayDescription = questionSet?.description ?: description

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
                    text = displayTitle,
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
                        InfoRow("Criador:", displayCreator)
                        InfoRow("Fonte:", displaySource)
                        InfoRow("Data:", displayDate)
                        InfoRow("Disciplina:", displayDiscipline)
                        InfoRow("Número de questões:", "$displayQuestionCount questões")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = displayDescription.ifBlank {
                                if (questionSet == null) defaultDescription else "Sem descrição disponível."
                            },
                            fontSize = 13.sp,
                            color = Color.Gray,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { firstQuestion?.let { onViewQuestionsClick(it.id.toString()) } },
                            enabled = firstQuestion != null,
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
            onClick = { firstQuestion?.let { onRespondClick(it.id.toString()) } },
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
            enabled = firstQuestion != null
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