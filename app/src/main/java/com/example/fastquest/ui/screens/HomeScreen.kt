// Home screen - Main screen displaying folders and questions list with search and filter
package com.example.fastquest.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Menu
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
import com.example.fastquest.data.repository.QuestionSetsRepository
import com.example.fastquest.data.repository.QuestionsRepository
import com.example.fastquest.ui.components.*
import com.example.fastquest.ui.theme.*
import com.example.fastquest.viewmodel.HomeViewModel
import com.example.fastquest.viewmodel.HomeViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

data class FolderItem(
    val id: String,
    val title: String,
    val description: String,
    val color: Color
)

data class QuestionItem(
    val id: String,
    val creator: String,
    val list: String,
    val source: String,
    val date: String,
    val discipline: String,
    val description: String
)

@Composable
fun HomeScreen(
    showFolders: Boolean = true,
    onFolderClick: (String) -> Unit = {},
    onQuestionClick: (String) -> Unit = {},
    onMenuClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    onCreateClick: () -> Unit = {}
) {
    val questionSetsRepository = remember {
        QuestionSetsRepository(ApiClient.questionSetsService)
    }
    val questionsRepository = remember {
        QuestionsRepository(ApiClient.questionsService)
    }
    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(questionSetsRepository, questionsRepository)
    )
    
    val questionSetsState by viewModel.questionSetsState.collectAsState()
    val questionsState by viewModel.questionsState.collectAsState()
    
    var searchText by remember { mutableStateOf("") }
    var isSearchingFolders by remember { mutableStateOf(showFolders) }
    
    // Load data on first composition
    LaunchedEffect(Unit) {
        viewModel.loadQuestionSets()
        viewModel.loadQuestions()
    }
    
    // Reload when switching between folders and questions
    LaunchedEffect(isSearchingFolders) {
        if (isSearchingFolders) {
            viewModel.loadQuestionSets()
        } else {
            viewModel.loadQuestions()
        }
    }
    
    // Map API data to UI models
    val folders = questionSetsState.questionSets.map { questionSet ->
        val colors = listOf(CardYellow, CardOrange, CardBlue, CardRed)
        FolderItem(
            id = questionSet.id.toString(),
            title = questionSet.name,
            description = questionSet.description,
            color = colors[questionSet.id % colors.size]
        )
    }
    
    val questions = questionsState.questions.map { question ->
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val year = try {
            // Parse ISO date string and extract year
            val date = dateFormat.parse(question.createdAt.substring(0, 10))
            SimpleDateFormat("yyyy", Locale.getDefault()).format(date ?: Date())
        } catch (e: Exception) {
            question.createdAt.substring(0, 4)
        }
        
        QuestionItem(
            id = question.id.toString(),
            creator = question.user?.name ?: "Desconhecido",
            list = "N/A", // Question doesn't have questionSetId in the model
            source = question.source?.name ?: "N/A",
            date = year,
            discipline = question.subject?.name ?: "Geral",
            description = question.statement
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(HomeBackgroundBlue)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top section with search and toggle
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Search bar with menu button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchBar(
                        value = searchText,
                        onValueChange = { searchText = it },
                        placeholder = if (isSearchingFolders) "Pesquise pastas" else "Pesquise perguntas",
                        modifier = Modifier.weight(1f)
                    )
                    
                    IconButton(
                        onClick = onMenuClick,
                        modifier = Modifier
                            .size(56.dp)
                            .background(TextFieldBackground, RoundedCornerShape(28.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.Black
                        )
                    }
                }

                // Toggle and filter button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Switch(
                            checked = isSearchingFolders,
                            onCheckedChange = { isSearchingFolders = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextPrimary,
                                checkedTrackColor = Color.Black,
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color.LightGray
                            )
                        )
                        Text(
                            text = if (isSearchingFolders) "Pesquisa de pastas" else "Pesquisa de perguntas",
                            color = TextPrimary,
                            fontSize = 14.sp
                        )
                    }

                    IconButton(
                        onClick = onFilterClick,
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.Black, RoundedCornerShape(20.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtros",
                            tint = TextPrimary
                        )
                    }
                }
            }

            // Content list
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                when {
                    isSearchingFolders && questionSetsState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = TextPrimary
                        )
                    }
                    !isSearchingFolders && questionsState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = TextPrimary
                        )
                    }
                    isSearchingFolders && questionSetsState.error != null -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = questionSetsState.error ?: "Erro ao carregar pastas",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 14.sp
                            )
                            Button(onClick = { viewModel.loadQuestionSets() }) {
                                Text("Tentar novamente")
                            }
                        }
                    }
                    !isSearchingFolders && questionsState.error != null -> {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = questionsState.error ?: "Erro ao carregar perguntas",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 14.sp
                            )
                            Button(onClick = { viewModel.loadQuestions() }) {
                                Text("Tentar novamente")
                            }
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (isSearchingFolders) {
                                if (folders.isEmpty()) {
                                    item {
                                        Text(
                                            text = "Nenhuma pasta encontrada",
                                            color = TextSecondary,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                } else {
                                    items(folders) { folder ->
                                        FolderCard(
                                            folder = folder,
                                            onClick = { onFolderClick(folder.id) }
                                        )
                                    }
                                }
                            } else {
                                if (questions.isEmpty()) {
                                    item {
                                        Text(
                                            text = "Nenhuma pergunta encontrada",
                                            color = TextSecondary,
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                } else {
                                    items(questions) { question ->
                                        QuestionCard(
                                            question = question,
                                            onClick = { onQuestionClick(question.id) }
                                        )
                                    }
                                }
                            }

                            item {
                                Spacer(modifier = Modifier.height(80.dp))
                            }
                        }
                    }
                }
            }
        }

        // Pagination at bottom
        val currentPage = if (isSearchingFolders) questionSetsState.currentPage else questionsState.currentPage
        val totalPages = if (isSearchingFolders) questionSetsState.totalPages else questionsState.totalPages
        
        if (totalPages > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (isSearchingFolders) {
                            viewModel.loadQuestionSets(currentPage - 1)
                        } else {
                            viewModel.loadQuestions(currentPage - 1)
                        }
                    },
                    enabled = currentPage > 1,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black, RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Anterior",
                        tint = if (currentPage > 1) TextPrimary else Color.Gray
                    )
                }

                // Show up to 3 page numbers
                val startPage = maxOf(1, currentPage - 1)
                val endPage = minOf(totalPages, startPage + 2)
                
                for (page in startPage..endPage) {
                    Surface(
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                if (isSearchingFolders) {
                                    viewModel.loadQuestionSets(page)
                                } else {
                                    viewModel.loadQuestions(page)
                                }
                            },
                        shape = RoundedCornerShape(20.dp),
                        color = if (page == currentPage) TextPrimary else Color.Black
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "$page",
                                color = if (page == currentPage) Color.Black else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        if (isSearchingFolders) {
                            viewModel.loadQuestionSets(currentPage + 1)
                        } else {
                            viewModel.loadQuestions(currentPage + 1)
                        }
                    },
                    enabled = currentPage < totalPages,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.Black, RoundedCornerShape(20.dp))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Próxima",
                        tint = if (currentPage < totalPages) TextPrimary else Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun FolderCard(
    folder: FolderItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = CardBackground
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Folder icon
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(folder.color)
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = folder.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = folder.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 3
                )
            }
        }
    }
}

@Composable
fun QuestionCard(
    question: QuestionItem,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = CardBackground
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Criador: ${question.creator}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "Lista: ${question.list}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "Fonte: ${question.source}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "Data: ${question.date}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "Disciplina: ${question.discipline}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = question.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 6
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    FastQuestTheme {
        HomeScreen()
    }
}