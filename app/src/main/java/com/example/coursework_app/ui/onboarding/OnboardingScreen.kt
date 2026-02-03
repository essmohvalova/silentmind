package com.example.coursework_app.ui.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.coursework_app.domain.model.CharacterType
import com.example.coursework_app.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.coursework_app.R
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Заголовок
        Text(
            text = stringResource(id = R.string.onboarding_title),
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        // Ввод имени
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(id = R.string.onboarding_name_label),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = uiState.name,
                onValueChange = { viewModel.onEvent(OnboardingViewModel.OnboardingEvent.OnNameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text(stringResource(id = R.string.onboarding_name_hint)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )
        }

        // Выбор персонажа
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(id = R.string.onboarding_character_label),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            CharacterSelection(
                selectedCharacter = uiState.selectedCharacter,
                onCharacterSelected = { character ->
                    viewModel.onEvent(OnboardingViewModel.OnboardingEvent.OnCharacterSelected(character))
                }
            )
        }

        // Кнопки действий
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onEvent(OnboardingViewModel.OnboardingEvent.OnSkipClicked)
                    onComplete()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(id = R.string.onboarding_skip))
            }

            Button(
                onClick = {
                    focusManager.clearFocus()
                    viewModel.onEvent(OnboardingViewModel.OnboardingEvent.OnCompleteClicked)
                    onComplete()
                },
                modifier = Modifier.weight(1f),
                enabled = uiState.isCompleteButtonEnabled
            ) {
                Text(stringResource(id = R.string.onboarding_complete))
            }
        }
    }
}

@Composable
fun CharacterSelection(
    selectedCharacter: CharacterType?,
    onCharacterSelected: (CharacterType) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(CharacterType.values()) { character ->
            CharacterItem(
                character = character,
                isSelected = selectedCharacter == character,
                onClick = { onCharacterSelected(character) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterItem(
    character: CharacterType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.size(100.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = character.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}