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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fastquest.data.local.TokenManager
import com.example.fastquest.data.network.ApiClient
import com.example.fastquest.data.repository.QuestionsRepository
import com.example.fastquest.ui.components.*
import com.example.fastquest.ui.theme.*
import com.example.fastquest.viewmodel.QuestionViewModel
import com.example.fastquest.viewmodel.QuestionViewModelFactory
import kotlinx.coroutines.delay

data class QuestionOption(
    val id: String,
    val text: String
)

@Composable
fun QuestionScreen(
    questionId: String,
    onBackClick: () -> Unit = {},
    onInfoClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onAnswerSubmitted: () -> Unit = {}
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val questionsRepository = remember {
        QuestionsRepository(ApiClient.questionsService, tokenManager)
    }
    val viewModel: QuestionViewModel = viewModel(
        factory = QuestionViewModelFactory(questionsRepository)
    )
    
    val questionState by viewModel.questionState.collectAsState()
    val selectedOptionId by viewModel.selectedOptionId.collectAsState()
    val timerSeconds by viewModel.timerSeconds.collectAsState()
    val isTimerRunning by viewModel.isTimerRunning.collectAsState()
    
    // Load question on first composition
    LaunchedEffect(questionId) {
        viewModel.loadQuestion(questionId)
    }
    
    // Timer effect
    LaunchedEffect(isTimerRunning) {
        while (isTimerRunning) {
            delay(1000)
            viewModel.incrementTimer()
        }
    }
    
    // Navigate on successful answer submission
    LaunchedEffect(questionState.answerSubmitted) {
        if (questionState.answerSubmitted) {
            onAnswerSubmitted()
        }
    }
    
    // Format timer text
    val hours = timerSeconds / 3600
    val minutes = (timerSeconds % 3600) / 60
    val seconds = timerSeconds % 60
    val timerText = String.format("%02d : %02d : %02d", hours, minutes, seconds)
    
    // Map API options to UI options
    val displayOptions = questionState.options.map { option ->
        QuestionOption(
            id = option.id,
            text = option.text
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(QuestionBackgroundRed)
    ) {
        when {
            questionState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = TextPrimary
                )
            }
            questionState.error != null -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = questionState.error ?: "Erro ao carregar questão",
                        color = TextPrimary,
                        fontSize = 14.sp
                    )
                    Button(onClick = { viewModel.loadQuestion(questionId) }) {
                        Text("Tentar novamente")
                    }
                }
            }
            questionState.question != null -> {
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
                            text = "Questão #${questionState.question?.id?.take(8) ?: ""}",
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
                                    onClick = { viewModel.pauseTimer() },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(ButtonRed, RoundedCornerShape(2.dp))
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.startTimer() },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Play",
                                        tint = Color.Black
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.resetTimer() },
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
                                text = questionState.question?.text ?: "",
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
                                isSelected = selectedOptionId == option.id,
                                onClick = { viewModel.selectOption(option.id) }
                            )
                        }

                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }

                // Confirm button
                Button(
                    onClick = { viewModel.submitAnswer() },
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
                    enabled = selectedOptionId != null && !questionState.isSubmitting
                ) {
                    if (questionState.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = TextPrimary
                        )
                    } else {
                        Text(
                            text = "CONFIRMAR",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
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
        QuestionScreen(questionId = "preview-id")
    }
}