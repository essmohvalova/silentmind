package com.example.coursework_app.ui.onboarding.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.coursework_app.R
import com.example.coursework_app.domain.model.CharacterType
import kotlinx.coroutines.launch

@Composable
fun CharacterCarousel(
    characters: List<CharacterType>,
    selectedCharacter: CharacterType,
    onCharacterSelected: (CharacterType) -> Unit,
    modifier: Modifier = Modifier
) {
    var currentIndex by remember { mutableIntStateOf(characters.indexOf(selectedCharacter).coerceAtLeast(0)) }
    val coroutineScope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    val characterDisplayMap = remember {
        mapOf(
            CharacterType.CAT to CharacterDisplay("Кот", R.raw.character_cat),
            CharacterType.DOG to CharacterDisplay("Собака", R.raw.character_dog),
            CharacterType.EMOJI to CharacterDisplay("Эмодзи", R.raw.character_emoji)
        )
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.Center
        ) {
            val currentCharacter = characters[currentIndex]
            val display = characterDisplayMap[currentCharacter]

            if (display != null) {
                CharacterCard(
                    character = currentCharacter,
                    displayName = display.displayName,
                    lottieRes = display.lottieRes,
                    isSelected = currentCharacter == selectedCharacter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .scale(scale.value)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        scale.animateTo(0.8f, tween(100))
                        currentIndex = if (currentIndex > 0) currentIndex - 1 else characters.size - 1
                        scale.animateTo(1f, tween(100))
                    }
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_media_previous),
                    contentDescription = "Предыдущий",
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                onClick = {
                    onCharacterSelected(characters[currentIndex])
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (characters[currentIndex] == selectedCharacter)
                        MaterialTheme.colorScheme.secondary
                    else
                        MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier.width(120.dp)
            ) {
                Text(
                    text = if (characters[currentIndex] == selectedCharacter)
                        "Выбрано"
                    else
                        "Выбрать",
                    fontSize = 14.sp
                )
            }

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        scale.animateTo(0.8f, tween(100))
                        currentIndex = if (currentIndex < characters.size - 1) currentIndex + 1 else 0
                        scale.animateTo(1f, tween(100))
                    }
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_media_next),
                    contentDescription = "Следующий",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(characters.size) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .padding(horizontal = 4.dp)
                        .clip(CircleShape)
                        .background(
                            color = if (index == currentIndex)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                        )
                )
            }
        }

        Text(
            text = characterDisplayMap[characters[currentIndex]]?.displayName ?: "",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun CharacterCard(
    character: CharacterType,
    displayName: String,
    lottieRes: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieRes))

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                composition = composition,
                modifier = Modifier.size(180.dp),
                iterations = Int.MAX_VALUE
            )
        }
    }
}

private data class CharacterDisplay(
    val displayName: String,
    val lottieRes: Int
)