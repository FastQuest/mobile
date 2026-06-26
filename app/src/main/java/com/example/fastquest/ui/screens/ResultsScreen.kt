// Results screen - Displays performance results with charts and statistics
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fastquest.data.local.TokenManager
import com.example.fastquest.data.network.ApiClient
import com.example.fastquest.data.repository.SubmissionsRepository
import com.example.fastquest.ui.components.*
import com.example.fastquest.ui.theme.*
import com.example.fastquest.viewmodel.ResultsViewModel
import com.example.fastquest.viewmodel.ResultsViewModelFactory

@Composable
fun ResultsScreen(
    userId: String? = null,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {}
) {
    val submissionsRepository = remember {
        SubmissionsRepository(
            ApiClient.submissionsService,
            ApiClient.answersService
        )
    }
    val viewModel: ResultsViewModel = viewModel(
        factory = ResultsViewModelFactory(submissionsRepository)
    )
    
    val performanceState by viewModel.performanceState.collectAsState()
    
    // Load performance data on first composition
    LaunchedEffect(userId) {
        viewModel.loadPerformance(userId)
    }
    
    val titleText = "SEUS RESULTADOS"
    val subtitleText = "Tudo sobre o seu desempenho"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ResultsBackgroundBlue)
    ) {
        when {
            performanceState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = TextPrimary
                )
            }
            performanceState.error != null -> {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = performanceState.error ?: "Erro ao carregar resultados",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = { viewModel.loadPerformance(userId) }) {
                        Text("Tentar novamente")
                    }
                }
            }
            else -> {
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

                    // Logo
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FastQuestLogo()

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = titleText,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = subtitleText,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }

                    // Performance metrics
                    val metrics = performanceState.performance
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Overall statistics card
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = CardBackground
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "Estatísticas Gerais",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    StatItem("Total", "${metrics?.totalQuestionsAnswered ?: 0}")
                                    StatItem("Corretas", "${metrics?.correctAnswers ?: 0}")
                                    val incorrect = (metrics?.totalQuestionsAnswered ?: 0) - (metrics?.correctAnswers ?: 0)
                                    StatItem("Incorretas", "$incorrect")
                                }
                                
                                Text(
                                    text = "Taxa de Acerto: ${String.format("%.1f", (metrics?.accuracy ?: 0f) * 100)}%",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ButtonDarkBlue
                                )
                            }
                        }

                        // Two smaller cards row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Average time card
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(180.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = CardBackground
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Tempo Médio",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "N/A",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ButtonDarkBlue
                                    )
                                    Text(
                                        text = "por questão",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            // Submissions card
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(180.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = CardBackground
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Submissões",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "${performanceState.submissions.size}",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CardOrange
                                    )
                                    Text(
                                        text = "realizadas",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }

                        // Performance by subject (if available)
                        if (!metrics?.bySubject.isNullOrEmpty()) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = CardBackground
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Desempenho por Disciplina",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    
                                    metrics?.bySubject?.take(5)?.forEach { subjectPerf ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = subjectPerf.subject,
                                                fontSize = 14.sp,
                                                color = TextDark,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text(
                                                text = "${String.format("%.1f", subjectPerf.accuracy * 100)}%",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ButtonDarkBlue
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ButtonDarkBlue
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ChartBar(
    height: androidx.compose.ui.unit.Dp,
    color: Color
) {
    Box(
        modifier = Modifier
            .width(24.dp)
            .height(height)
            .background(color, RoundedCornerShape(4.dp))
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResultsScreenPreview() {
    FastQuestTheme {
        ResultsScreen()
    }
}