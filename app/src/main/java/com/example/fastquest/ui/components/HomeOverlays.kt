// Overlays used by the Home search screen: the Filtros dialog and the hamburger menu dropdown
package com.example.fastquest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.fastquest.data.model.response.QuestionFilters
import com.example.fastquest.ui.state.QuestionFilterSelection
import com.example.fastquest.ui.theme.*

private val FilterDialogBackground = Color(0xFFEDEDED)
private val ChipColors = listOf(TagRed, TagBlue, TagOrange, TagBrown)

/**
 * "FILTROS" dialog matching the Home - Filtro design.
 * In pastas mode only sort order is meaningful (question sets have no subject/source/year).
 */
@Composable
fun FilterDialog(
    isQuestionsMode: Boolean,
    availableFilters: QuestionFilters,
    selection: QuestionFilterSelection,
    questionSetsOrderBy: String,
    onSelectionChange: (QuestionFilterSelection) -> Unit,
    onQuestionSetsOrderByChange: (String) -> Unit,
    onReset: () -> Unit,
    onDismiss: () -> Unit
) {
    val orderByOptions = if (isQuestionsMode) {
        listOf("created_at desc" to "Mais recentes", "created_at asc" to "Mais antigas")
    } else {
        listOf(
            "created_at desc" to "Mais recentes",
            "created_at asc" to "Mais antigas",
            "name asc" to "Nome (A-Z)",
            "name desc" to "Nome (Z-A)"
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(20.dp))
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "FILTROS",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Fechar", tint = Color.White)
                }
            }

            // Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(FilterDialogBackground)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterDropdown(
                    placeholder = "Ordenar por",
                    selectedLabel = orderByOptions.firstOrNull { it.first == if (isQuestionsMode) selection.orderBy else questionSetsOrderBy }?.second,
                    options = orderByOptions.map { it.second },
                    onOptionSelected = { label ->
                        val value = orderByOptions.first { it.second == label }.first
                        if (isQuestionsMode) {
                            onSelectionChange(selection.copy(orderBy = value))
                        } else {
                            onQuestionSetsOrderByChange(value)
                        }
                    }
                )

                if (isQuestionsMode) {
                    FilterDropdown(
                        placeholder = "Fonte",
                        selectedLabel = selection.sourceName,
                        options = availableFilters.sources.map { it.name },
                        onOptionSelected = { label ->
                            val item = availableFilters.sources.first { it.name == label }
                            onSelectionChange(selection.copy(sourceId = item.id, sourceName = item.name))
                        }
                    )

                    FilterDropdown(
                        placeholder = "Data",
                        selectedLabel = selection.year?.toString(),
                        options = availableFilters.years.map { it.toString() },
                        onOptionSelected = { label ->
                            onSelectionChange(selection.copy(year = label.toIntOrNull()))
                        }
                    )

                    FilterDropdown(
                        placeholder = "Disciplina",
                        selectedLabel = selection.subjectName,
                        options = availableFilters.subjects.map { it.name },
                        onOptionSelected = { label ->
                            val item = availableFilters.subjects.first { it.name == label }
                            onSelectionChange(selection.copy(subjectId = item.id, subjectName = item.name))
                        }
                    )

                    AssuntoInput(
                        topics = selection.topics,
                        onAddTopic = { topic ->
                            onSelectionChange(selection.copy(topics = selection.topics + topic))
                        },
                        onRemoveTopic = { topic ->
                            onSelectionChange(selection.copy(topics = selection.topics - topic))
                        }
                    )
                }

                UnderlinedTextButton(
                    text = "Resetar Filtros",
                    onClick = onReset,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
private fun FilterDropdown(
    placeholder: String,
    selectedLabel: String?,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = options.isNotEmpty()) { expanded = true },
            shape = RoundedCornerShape(28.dp),
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedLabel ?: placeholder,
                    color = if (selectedLabel != null) Color.Black else Color.Gray,
                    fontSize = 16.sp
                )
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.Black)
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun AssuntoInput(
    topics: List<String>,
    onAddTopic: (String) -> Unit,
    onRemoveTopic: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Assunto", color = Color.Gray, fontSize = 16.sp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                if (text.isNotBlank() && text !in topics) {
                    onAddTopic(text.trim())
                }
                text = ""
            })
        )

        if (topics.isNotEmpty()) {
            FlowRowChips(topics = topics, onRemoveTopic = onRemoveTopic)
        }
    }
}

@Composable
private fun FlowRowChips(topics: List<String>, onRemoveTopic: (String) -> Unit) {
    // Simple wrap layout using Rows chunked by a fixed count, avoiding an extra layout dependency
    val rows = topics.chunked(2)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        rows.forEach { rowTopics ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rowTopics.forEach { topic ->
                    val color = ChipColors[topics.indexOf(topic) % ChipColors.size]
                    FilterChip(
                        text = topic,
                        color = color,
                        onRemove = { onRemoveTopic(topic) }
                    )
                }
            }
        }
    }
}

/**
 * Hamburger menu dropdown matching the "Home - Menu aberto" design: Home / Criar / Perfil.
 * Must be placed inside a Box together with the anchor (hamburger) button.
 */
@Composable
fun HomeMenuDropdown(
    expanded: Boolean,
    onHomeClick: () -> Unit,
    onCreateClick: () -> Unit,
    onProfileClick: () -> Unit,
    onDismiss: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
        modifier = Modifier
            .width(180.dp)
            .background(Color(0xFFF0F0F0))
    ) {
        HomeMenuItem(text = "Home", onClick = { onDismiss(); onHomeClick() })
        HorizontalDivider(color = Color(0xFFD0D0D0))
        HomeMenuItem(text = "Criar", onClick = { onDismiss(); onCreateClick() })
        HorizontalDivider(color = Color(0xFFD0D0D0))
        HomeMenuItem(text = "Perfil", onClick = { onDismiss(); onProfileClick() })
    }
}

@Composable
private fun HomeMenuItem(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = Color.Black, fontSize = 16.sp)
    }
}
