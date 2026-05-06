package com.example.coursework_app.ui.practice

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Button
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
fun AffirmationPracticeScreen(
    onBackClick: () -> Unit,
    viewModel: AffirmationPracticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AppBarTitle(
            title = "Аффирмации",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                uiState.emotionLabel?.let { emotion ->
                    Text(
                        text = "Последнее настроение: $emotion",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.quote,
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }

                uiState.hint?.let { hint ->
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Button(onClick = onBackClick) {
                Text("Назад")
            }
        }
    }
}
