package com.example.coursework_app.ui.practice

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coursework_app.R

enum class IllustrationType { SunClouds, Leaves, Waves, Glow, Stars }

data class AffirmationTheme(
    val gradient: List<Color>,
    val accent: Color,
    val illustrationType: IllustrationType
)

@Composable
fun AffirmationPracticeScreen(
    onBackClick: () -> Unit,
    viewModel: AffirmationPracticeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val theme = remember(uiState.emotionLabel) { themeForMood(uiState.emotionLabel) }
    val pagerItems = remember { mutableStateListOf<String>() }

    LaunchedEffect(uiState.quote) {
        if (uiState.quote.isNotBlank() && (pagerItems.isEmpty() || pagerItems.last() != uiState.quote)) {
            pagerItems.add(uiState.quote)
        }
    }
    val pagerState = rememberPagerState { pagerItems.size.coerceAtLeast(1) }

    LaunchedEffect(pagerItems.size) {
        if (pagerItems.isNotEmpty()) pagerState.animateScrollToPage(pagerItems.lastIndex)
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = theme.accent)
        }
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { AffirmationHeader(onBackClick = onBackClick) }
        item { MoodContextCard(emotionLabel = uiState.emotionLabel, theme = theme) }
        item {
            AffirmationHeroCard(
                pagerItems = pagerItems.ifEmpty { listOf(uiState.quote) },
                pagerState = pagerState,
                theme = theme
            )
        }
        item {
            AffirmationPagerControls(
                accent = theme.accent,
                onNewThought = { viewModel.generateNewAffirmation() }
            )
        }
        item {
            uiState.hint?.let { hint ->
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        item { MotivationFooterCard() }
    }
}

@Composable
fun AffirmationHeader(onBackClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        HeaderSquareButton(icon = ImageVector.vectorResource(id = R.drawable.ic_arrow_left), onClick = onBackClick)
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Аффирмации",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.SemiBold)
            )
            Text(
                text = "Поддержка для твоего текущего состояния",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
        Box(modifier = Modifier.size(44.dp))
    }
}

@Composable
private fun HeaderSquareButton(icon: ImageVector, onClick: () -> Unit) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, tween(120), label = "backScale")
    Box(
        modifier = Modifier
            .size(44.dp)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(10.dp, RoundedCornerShape(14.dp), ambientColor = Color(0x22000000))
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF4ECE1))
            .border(1.dp, Color(0x1E000000), RoundedCornerShape(14.dp))
            .clickable(interactionSource = interaction, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = Color(0xFF3F3A34)
        )
    }
}

@Composable
fun MoodContextCard(emotionLabel: String?, theme: AffirmationTheme) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFFF7F1E8), Color(0xFFFAF6EE))))
            .border(1.dp, Color(0x1A655C4A), RoundedCornerShape(24.dp))
            .padding(horizontal = 12.dp, vertical = 9.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "${moodEmoji(emotionLabel)} Последнее настроение: ${emotionLabel ?: "Не определено"}",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Medium),
                    color = Color(0xFF5E5A54)
                )
                Text(
                    text = "Сегодня тебе подойдут теплые и вдохновляющие мысли",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF7B756C),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(theme.accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) { Text(text = "☀", fontSize = 14.sp, color = theme.accent.copy(alpha = 0.65f)) }
        }
    }
}

@Composable
fun AffirmationHeroCard(pagerItems: List<String>, pagerState: PagerState, theme: AffirmationTheme) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(308.dp)
            .shadow(8.dp, RoundedCornerShape(30.dp), ambientColor = Color(0x12000000))
            .clip(RoundedCornerShape(30.dp))
            .background(Brush.verticalGradient(theme.gradient))
            .border(1.dp, Color(0x14FFFFFF), RoundedCornerShape(30.dp))
            .padding(horizontal = 24.dp, vertical = 18.dp)
    ) {
        MinimalDecorativeLayer(theme = theme)
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth().weight(1f)) { page ->
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = pagerItems.getOrElse(page) { "" },
                        style = MaterialTheme.typography.headlineSmall.copy(
                            lineHeight = 33.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(0xFF44413D),
                        textAlign = TextAlign.Center
                    )
                }
            }
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(2.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(theme.accent.copy(alpha = 0.28f))
            )
        }
    }
}

@Composable
private fun MinimalDecorativeLayer(theme: AffirmationTheme) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = 10.dp)
                .size(width = 92.dp, height = 20.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(theme.accent.copy(alpha = 0.08f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 10.dp, top = 8.dp)
                .size(8.dp)
                .clip(CircleShape)
                .background(theme.accent.copy(alpha = 0.12f))
        )
    }
}

@Composable
fun AffirmationPagerControls(
    accent: Color,
    onNewThought: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Button(
            onClick = onNewThought,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(999.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = accent.copy(alpha = 0.82f),
                contentColor = Color(0xFFFAF9F7)
            )
        ) {
            Text(
                text = "↻  Новая мысль",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
    }
}

@Composable
fun MotivationFooterCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(Brush.horizontalGradient(listOf(Color(0xFFECF7EE), Color(0xFFF5F1E8))))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "❀", color = Color(0xFF688066), fontSize = 16.sp)
            Text(
                text = "Аффирмации работают лучше, когда повторяются каждый день 🤍",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF5E6E5A), fontWeight = FontWeight.Medium)
            )
        }
    }
}

private fun themeForMood(emotion: String?): AffirmationTheme = when (emotion?.trim()?.lowercase()) {
    "радость" -> AffirmationTheme(
        gradient = listOf(Color(0xFFFDF9F0), Color(0xFFFDF6EA), Color(0xFFFBF3E4)),
        accent = Color(0xFFC39A5B),
        illustrationType = IllustrationType.SunClouds
    )
    "спокойствие" -> AffirmationTheme(
        gradient = listOf(Color(0xFFFAF8F1), Color(0xFFF4F7F0), Color(0xFFEFF4EE)),
        accent = Color(0xFF7DA79B),
        illustrationType = IllustrationType.Waves
    )
    "грусть" -> AffirmationTheme(
        gradient = listOf(Color(0xFFF9F8F3), Color(0xFFF1F4F8), Color(0xFFECEFF5)),
        accent = Color(0xFF8299B8),
        illustrationType = IllustrationType.Glow
    )
    "тревога" -> AffirmationTheme(
        gradient = listOf(Color(0xFFF9F6F1), Color(0xFFF3EFF5), Color(0xFFEEE8F2)),
        accent = Color(0xFF8E7AA8),
        illustrationType = IllustrationType.Stars
    )
    "усталость" -> AffirmationTheme(
        gradient = listOf(Color(0xFFF8F6F3), Color(0xFFF1EDF2), Color(0xFFEBE6EE)),
        accent = Color(0xFF8A7897),
        illustrationType = IllustrationType.Leaves
    )
    "злость" -> AffirmationTheme(
        gradient = listOf(Color(0xFFFBF7F1), Color(0xFFF5EFEB), Color(0xFFF0E8E3)),
        accent = Color(0xFFB08477),
        illustrationType = IllustrationType.Glow
    )
    else -> AffirmationTheme(
        gradient = listOf(Color(0xFFFBF7F0), Color(0xFFF6F1EA), Color(0xFFF1ECE5)),
        accent = Color(0xFF8A7A97),
        illustrationType = IllustrationType.Glow
    )
}

private fun moodEmoji(emotion: String?): String = when (emotion?.trim()?.lowercase()) {
    "радость" -> "🌞"
    "спокойствие" -> "🍃"
    "грусть" -> "☁"
    "тревога" -> "💜"
    "усталость" -> "🌙"
    else -> "🤍"
}
