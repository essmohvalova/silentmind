package com.example.coursework_app.ui.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coursework_app.R
import com.example.coursework_app.ui.onboarding.components.OnboardingIndicator
import com.example.coursework_app.ui.onboarding.pages.CharacterSelectionScreen
import com.example.coursework_app.ui.onboarding.pages.CompletionScreen
import com.example.coursework_app.ui.onboarding.pages.WelcomeScreen

@Composable
fun OnboardingScreen(viewModel: OnboardingViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            when (uiState.currentPage) {
                0 -> WelcomeScreen(
                    onNextClicked = { name, email ->
                        viewModel.updateNameAndEmail(name, email)
                        viewModel.nextPage()
                    }
                )
                1 -> CharacterSelectionScreen(
                    selectedCharacter = uiState.contentData.selectedCharacter,
                    onCharacterSelected = { viewModel.selectCharacter(it) },
                    onNextClicked = { viewModel.nextPage() }
                )
                2 -> CompletionScreen(
                    userName = uiState.contentData.userName,
                    onFinishClicked = { viewModel.completeOnboarding() }
                )
            }
        }

        OnboardingIndicator(
            currentPage = uiState.currentPage,
            totalPages = 3,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        if (uiState.currentPage > 0) {
            Button(
                onClick = { viewModel.previousPage() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            ) {
                Text(stringResource(R.string.back_button))
            }
        }
    }
}
