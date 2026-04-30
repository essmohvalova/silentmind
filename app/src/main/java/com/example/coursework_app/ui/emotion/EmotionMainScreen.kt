package com.example.coursework_app.ui.emotion

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework_app.domain.model.emotion.Emotion

@Composable
fun EmotionMainScreen(
    viewModel: EmotionViewModel,
    onNextClick: () -> Unit,
) {
    var selectedEmotionId by remember { mutableStateOf(viewModel.selectedEmotion?.id) }
    val emotions = Emotion.defaultList
    var intensity by remember { mutableIntStateOf(viewModel.intensity) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = "Как ты себя чувствуешь?",
                style = MaterialTheme.typography.headlineMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Интенсивность: $intensity",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(12.dp))

            EmotionIntensityDots(
                value = intensity,
                onValueChange = {
                    intensity = it
                    viewModel.changeIntensity(it)
                },
            )

            Spacer(modifier = Modifier.height(24.dp))

            emotions.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    row.forEach { emotion ->
                        EmotionItem(
                            emotion = emotion,
                            selected = selectedEmotionId == emotion.id,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                selectedEmotionId = emotion.id
                                viewModel.selectEmotion(emotion)
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedEmotionId != null,
            onClick = onNextClick,
        ) {
            Text("Далее")
        }
    }
}

@Composable
private fun EmotionIntensityDots(
    value: Int,
    onValueChange: (Int) -> Unit,
    max: Int = 5,
) {
    val dotButtonSize = 24.dp
    val dotsSpacing = 10.dp
    val dotsRowWidth = dotButtonSize * max + dotsSpacing * (max - 1)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.width(dotsRowWidth),
            horizontalArrangement = Arrangement.spacedBy(dotsSpacing)
        ) {
            (1..max).forEach { level ->
                IconButton(
                    onClick = { onValueChange(level) },
                    modifier = Modifier.size(dotButtonSize)
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(
                                color = if (level <= value) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                                },
                                shape = CircleShape
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.width(dotsRowWidth),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "слабо",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "сильно",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmotionItem(
    emotion: Emotion,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(96.dp),
        shape = RoundedCornerShape(20.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (selected) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = emotion.iconRes),
                contentDescription = null,
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = emotion.text,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}