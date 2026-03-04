package com.example.coursework_app.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coursework_app.domain.model.CharacterType
import com.example.coursework_app.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.coursework_app.ui.onboarding.components.OnboardingIndicator

@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentPage by viewModel.currentPage.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.onboardingCompleted) {
        if (uiState.onboardingCompleted) {
            onOnboardingComplete()
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) {
                when (currentPage) {
                    0 -> WelcomeScreen(
                        onNextClicked = { name, email ->
                            viewModel.updateName(name)
                            viewModel.updateEmail(email)
                            viewModel.nextPage()
                        }
                    )
                    1 -> CharacterSelectionScreen(
                        selectedCharacter = viewModel.uiState.value.user?.selectedCharacter ?: CharacterType.CAT,
                        onCharacterSelected = { viewModel.selectCharacter(it) },
                        onNextClicked = { viewModel.nextPage() }
                    )
                    2 -> CompletionScreen(
                        userName = viewModel.uiState.value.user?.name ?: "",
                        onFinishClicked = { viewModel.completeOnboarding() }
                    )
                }
            }

            OnboardingIndicator(
                currentPage = currentPage,
                totalPages = 3,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            if (currentPage > 0) {
                Button(
                    onClick = { viewModel.previousPage() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 16.dp)
                ) {
                    Text("Назад")
                }
            }
        }
    }
}