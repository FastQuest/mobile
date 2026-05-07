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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fastquest.ui.components.*
import com.example.fastquest.ui.theme.*

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
    var searchText by remember { mutableStateOf("") }
    var isSearchingFolders by remember { mutableStateOf(showFolders) }

    // Sample data
    val folders = remember {
        listOf(
            FolderItem("1", "Título", "Esta lista contém uma série de questões fundamentais e de aprofundamento sobre o direito criminal, abordando desde conceitos básicos até tópicos mais complexos relacionados à legislação...", CardYellow),
            FolderItem("2", "Título", "Esta lista contém uma série de questões fundamentais e de aprofundamento sobre o direito criminal, abordando desde conceitos básicos até tópicos mais complexos relacionados à legislação...", CardOrange),
            FolderItem("3", "Título", "Esta lista contém uma série de questões fundamentais e de aprofundamento sobre o direito criminal, abordando desde conceitos básicos até tópicos mais complexos relacionados à legislação...", CardBlue),
            FolderItem("4", "Título", "Esta lista contém uma série de questões fundamentais e de aprofundamento sobre o direito criminal, abordando desde conceitos básicos até tópicos mais complexos relacionados à legislação...", CardRed)
        )
    }

    val questions = remember {
        listOf(
            QuestionItem("1", "Joãozinho123", "Perguntas Tribut...", "OAB", "2024", "Penal", "Esta lista contém uma série de questões fundamentais e de aprofundamento sobre o direito criminal, abordando desde conceitos básicos até tópicos mais complexos relacionados à legislação..."),
            QuestionItem("2", "Joãozinho123", "Perguntas Tribut...", "OAB", "2024", "Penal", "Esta lista contém uma série de questões fundamentais e de aprofundamento sobre o direito criminal, abordando desde conceitos básicos até tópicos mais complexos relacionados à legislação..."),
            QuestionItem("3", "Joãozinho123", "Perguntas Tribut...", "OAB", "2024", "Penal", "Esta lista contém uma série de questões fundamentais e de aprofundamento sobre o direito criminal, abordando desde conceitos básicos até tópicos mais complexos relacionados à legislação..."),
            QuestionItem("4", "Joãozinho123", "Perguntas Tribut...", "OAB", "2024", "Penal", "Esta lista contém uma série de questões fundamentais e de aprofundamento sobre o direito criminal, abordando desde conceitos básicos até tópicos mais complexos relacionados à legislação...")
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (isSearchingFolders) {
                    items(folders) { folder ->
                        FolderCard(
                            folder = folder,
                            onClick = { onFolderClick(folder.id) }
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

                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }

        // Pagination at bottom
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { /* Previous page */ },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black, RoundedCornerShape(20.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Anterior",
                    tint = TextPrimary
                )
            }

            repeat(3) { index ->
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { /* Go to page */ },
                    shape = RoundedCornerShape(20.dp),
                    color = if (index == 0) TextPrimary else Color.Black
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${index + 1}",
                            color = if (index == 0) Color.Black else TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            IconButton(
                onClick = { /* Next page */ },
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Black, RoundedCornerShape(20.dp))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Próxima",
                    tint = TextPrimary
                )
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

// Made with Bob
