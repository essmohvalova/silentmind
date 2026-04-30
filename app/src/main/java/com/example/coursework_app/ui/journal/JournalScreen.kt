package com.example.coursework_app.ui.journal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coursework_app.ui.components.AppBarTitle
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JournalScreen(
    viewModel: JournalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppBarTitle(
            title = "Журнал записей",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        JournalScreenContent(
            uiState = uiState,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun JournalScreenContent(
    uiState: JournalUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text(
            text = "Количество записей: ${uiState.entriesCount}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(uiState.entries, key = { it.id }) { entry ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = entry.emotion,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Интенсивность: ${entry.intensity}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        entry.text?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Text(
                            text = formatDate(entry.createdAt),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

private fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    return formatter.format(Date(timestamp))
}