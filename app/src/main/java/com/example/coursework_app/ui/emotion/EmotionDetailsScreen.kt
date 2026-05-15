package com.example.coursework_app.ui.emotion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionDetailsScreen(
    viewModel: EmotionViewModel,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            TextButton(onClick = onBackClick) {
                Text("Назад")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Расскажи чуть подробнее",
                style = MaterialTheme.typography.headlineMedium,
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                value = uiState.text,
                onValueChange = viewModel::changeText,
                label = {
                    Text("Что случилось?")
                },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                ),
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSaving,
            onClick = onSaveClick,
        ) {
            Text("Сохранить")
        }
    }
}