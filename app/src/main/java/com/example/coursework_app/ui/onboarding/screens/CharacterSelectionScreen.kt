package com.example.coursework_app.ui.onboarding.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.coursework_app.R
import com.example.coursework_app.domain.model.CharacterType
import com.example.coursework_app.ui.onboarding.components.AnimatedCharacter
import com.example.coursework_app.ui.onboarding.components.NextButton

@Composable
fun CharacterSelectionScreen(
    selectedCharacter: CharacterType,
    onCharacterSelected: (CharacterType) -> Unit,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Кнопка "Назад" (опционально)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back), // Создайте иконку
                        contentDescription = "Назад"
                    )
                }
            }

            // Заголовок
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Выберите своего персонажа",
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Он будет сопровождать вас в путешествии\nпо миру эмоций",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Анимированный персонаж с стрелочками для переключения
            CharacterCarousel(
                selectedCharacter = selectedCharacter,
                onCharacterSelected = onCharacterSelected
            )

            // Имя текущего персонажа
            Text(
                text = getCharacterName(selectedCharacter),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка "Далее"
            NextButton(
                text = "Далее",
                onClick = onNext
            )
        }
    }
}

@Composable
fun CharacterCarousel(
    selectedCharacter: CharacterType,
    onCharacterSelected: (CharacterType) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Стрелка влево
        IconButton(
            onClick = {
                val previousCharacter = getPreviousCharacter(selectedCharacter)
                onCharacterSelected(previousCharacter)
            },
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left), // Создайте иконку
                contentDescription = "Предыдущий персонаж",
                modifier = Modifier.size(48.dp)
            )
        }

        // Анимированный персонаж (центр)
        AnimatedCharacter(
            characterType = selectedCharacter,
            modifier = Modifier.weight(1f)
        )

        // Стрелка вправо
        IconButton(
            onClick = {
                val nextCharacter = getNextCharacter(selectedCharacter)
                onCharacterSelected(nextCharacter)
            },
            modifier = Modifier.size(64.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right), // Создайте иконку
                contentDescription = "Следующий персонаж",
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

// Вспомогательные функции для навигации по персонажам
private fun getNextCharacter(current: CharacterType): CharacterType {
    return when (current) {
        CharacterType.CAT -> CharacterType.DOG
        CharacterType.DOG -> CharacterType.EMOJI
        CharacterType.EMOJI -> CharacterType.CAT
    }
}

private fun getPreviousCharacter(current: CharacterType): CharacterType {
    return when (current) {
        CharacterType.CAT -> CharacterType.EMOJI
        CharacterType.DOG -> CharacterType.CAT
        CharacterType.EMOJI -> CharacterType.DOG
    }
}

private fun getCharacterName(character: CharacterType): String {
    return when (character) {
        CharacterType.CAT -> "Котик"
        CharacterType.DOG -> "Собачка"
        CharacterType.EMOJI -> "Эмодзи"
}
}