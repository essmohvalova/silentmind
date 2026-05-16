package com.example.coursework_app.ui.emotion

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import com.example.coursework_app.domain.model.emotion.Emotion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmotionDetailsScreen(
    viewModel: EmotionViewModel,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val selectedEmotion = uiState.selectedEmotion
    val accentColor = selectedEmotion.toAccentColor()
    val supportText = selectedEmotion.toSupportText()
    val scrollState = rememberScrollState()
    val maxChars = 500

    val quickTags = remember {
        listOf("Сон", "Работа", "Учеба", "Люди", "Здоровье", "Погода", "Отдых", "Еда")
    }
    var selectedTags by remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .imePadding()
            .navigationBarsPadding(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 20.dp),
        ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Surface(
                        onClick = onBackClick,
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(999.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                            )
                            Text(text = "Назад")
                        }
                    }
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = "Запись настроения",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = accentColor.copy(alpha = 0.16f),
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 18.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(accentColor.copy(alpha = 0.26f), CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            selectedEmotion?.let { emotion ->
                                Image(
                                    painter = painterResource(id = emotion.iconRes),
                                    contentDescription = emotion.text,
                                    modifier = Modifier.size(30.dp),
                                )
                            }
                        }
                        Column {
                            Text(
                                text = selectedEmotion?.text ?: "Текущее состояние",
                                style = MaterialTheme.typography.titleLarge,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Интенсивность: ${uiState.intensity}/5",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = supportText,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.75f),
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = accentColor.copy(alpha = 0.25f),
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text(
                            text = "Что повлияло на это состояние?",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            value = uiState.text,
                            onValueChange = {
                                viewModel.changeText(it.take(maxChars))
                            },
                            placeholder = {
                                Text("Опиши, что произошло или что ты сейчас чувствуешь…")
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = accentColor.copy(alpha = 0.06f),
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                                focusedBorderColor = accentColor.copy(alpha = 0.45f),
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                            ),
                            shape = RoundedCornerShape(18.dp),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                            ),
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "${uiState.text.length}/$maxChars",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.align(Alignment.End),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "Что могло повлиять?",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.height(8.dp))
                EmotionTagsFlow(
                    quickTags = quickTags,
                    selectedTags = selectedTags,
                    accentColor = accentColor,
                    onTagClick = { tag, selected ->
                        selectedTags = if (selected) {
                            selectedTags - tag
                        } else {
                            selectedTags + tag
                        }
                        // TODO: persist selectedTags in MoodEntry when the model adds tags support.
                    },
                )

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    onClick = onSaveClick,
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = Color.White,
                    ),
                ) {
                    Text("Сохранить")
                }
                Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

private fun Emotion?.toSupportText(): String = when (this?.id) {
    "joy" -> "Запомни этот хороший момент."
    "calm" -> "Побудь немного в этом состоянии."
    "sadness" -> "Это чувство тоже важно заметить."
    "anxiety" -> "Попробуй мягко описать, что тебя беспокоит."
    "anger" -> "Дай этому чувству безопасное место."
    "fatigue" -> "Твоему телу может быть нужен отдых."
    else -> "Отметь свои ощущения, чтобы лучше понимать себя."
}

private fun Emotion?.toAccentColor(): Color = when (this?.id) {
    "joy" -> Color(0xFFE8B54B)
    "calm" -> Color(0xFF7FC9B3)
    "sadness" -> Color(0xFF7EA7D8)
    "anxiety" -> Color(0xFFB7A7D9)
    "anger" -> Color(0xFFD5897A)
    "fatigue" -> Color(0xFF9B8FB8)
    else -> Color(0xFFB29A7A)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmotionTagsFlow(
    quickTags: List<String>,
    selectedTags: Set<String>,
    accentColor: Color,
    onTagClick: (String, Boolean) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        quickTags.forEach { tag ->
            val selected = tag in selectedTags
            val scale by animateFloatAsState(
                targetValue = if (selected) 1f else 0.985f,
                animationSpec = tween(200),
                label = "tagScale",
            )
            val alpha by animateFloatAsState(
                targetValue = if (selected) 1f else 0.95f,
                animationSpec = tween(200),
                label = "tagAlpha",
            )

            val unselectedBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
            val selectedBgTarget = accentColor
            val backgroundColor by animateColorAsState(
                targetValue = if (selected) selectedBgTarget else unselectedBg,
                animationSpec = tween(200),
                label = "tagBg",
            )
            val borderColor by animateColorAsState(
                targetValue = if (selected) accentColor else Color.Transparent,
                animationSpec = tween(200),
                label = "tagBorder",
            )
            val textColor by animateColorAsState(
                targetValue = if (selected) {
                    Color.White
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f)
                },
                animationSpec = tween(200),
                label = "tagText",
            )

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = backgroundColor,
                tonalElevation = if (selected) 1.dp else 0.dp,
                shadowElevation = if (selected) 1.dp else 0.dp,
                border = BorderStroke(
                    width = if (selected) 1.dp else 0.dp,
                    color = borderColor,
                ),
                modifier = Modifier
                    .scale(scale)
                    .alpha(alpha)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = LocalIndication.current,
                    ) { onTagClick(tag, selected) },
            ) {
                Text(
                    text = tag,
                    color = textColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                )
            }
        }
    }
}