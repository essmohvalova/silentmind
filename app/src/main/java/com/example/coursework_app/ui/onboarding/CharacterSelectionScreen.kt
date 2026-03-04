package com.example.coursework_app.ui.onboarding

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework_app.domain.model.CharacterType
import com.example.coursework_app.ui.onboarding.components.CharacterCarousel
import com.example.coursework_app.ui.components.TopAppBarTitle

@Composable
fun CharacterSelectionScreen(
    selectedCharacter: CharacterType,
    onCharacterSelected: (CharacterType) -> Unit,
    onNextClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBarTitle(
            title = "Выберите персонажа",
            fontSize = 28,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Кто будет вашим помощником?",
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        CharacterCarousel(
            characters = CharacterType.entries,
            selectedCharacter = selectedCharacter,
            onCharacterSelected = onCharacterSelected,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNextClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Продолжить", fontSize = 16.sp)
        }
    }
}