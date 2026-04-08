package com.example.coursework_app.ui.emotion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coursework_app.ui.components.AppBarTitle

@Composable
fun EmotionScreen(
    viewModel: EmotionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AppBarTitle(
            title = "Выбор эмоций",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        EmotionScreenContent(
            uiState = uiState,
            onEmotionSelected = { viewModel.selectEmotion(it) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun EmotionScreenContent(
    uiState: EmotionUiState,
    onEmotionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            // смапить все на этапе формирования стейта
            text = "Доступные эмоции: ${uiState.availableEmotions.joinToString(" ")}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        if (uiState.selectedEmotionId != null) {
            Text(
                text = "Выбрано: ${uiState.selectedEmotionId}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}