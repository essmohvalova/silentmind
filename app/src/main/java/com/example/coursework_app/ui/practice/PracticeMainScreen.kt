package com.example.coursework_app.ui.practice

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.coursework_app.R

data class PracticeCardUi(
    val title: String,
    val subtitle: String,
    val accentColors: List<Color>,
    val icon: ImageVector,
    val badgeText: String = ""
)

@Composable
fun PracticeMainScreen(
    onBreathingClick: () -> Unit,
    onAffirmationClick: () -> Unit,
    onRandomPracticeClick: () -> Unit
) {
    val cards = listOf(
        PracticeCardUi(
            title = "Дыхание",
            subtitle = "Успокой ум и тело с помощью дыхательных упражнений",
            accentColors = listOf(Color(0xFFEAF6FF), Color(0xFFD6EAFF), Color(0xFFCAE6FF)),
            icon = ImageVector.vectorResource(id = R.drawable.ic_navbar_practice)
        ),
        PracticeCardUi(
            title = "Аффирмации",
            subtitle = "Позитивные установки на каждый день",
            accentColors = listOf(Color(0xFFE9F7EE), Color(0xFFD4ECD9), Color(0xFFC3E4CC)),
            icon = ImageVector.vectorResource(id = R.drawable.ic_navbar_mood)
        ),
        PracticeCardUi(
            title = "Случайная практика",
            subtitle = "Получите практику, которая подойдет вам прямо сейчас",
            accentColors = listOf(Color(0xFFF1EBFF), Color(0xFFE5DAFF), Color(0xFFDCCEFF)),
            icon = ImageVector.vectorResource(id = R.drawable.ic_practice_book),
            badgeText = "Каждый день новое"
        )
    )

    val clickActions = listOf(onBreathingClick, onAffirmationClick, onRandomPracticeClick)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                top = 20.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { PracticeHeader() }
            itemsIndexed(cards) { index, card ->
                PracticeHeroCard(
                    card = card,
                    onClick = clickActions[index],
                    index = index
                )
            }
            item { PracticeFooterCard() }
        }
    }
}

@Composable
fun PracticeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(30.dp),
                ambientColor = Color(0x26000000),
                spotColor = Color(0x14000000)
            )
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFFFEFCF8))
            .border(1.dp, Color(0xFFEDE7DE), RoundedCornerShape(30.dp))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Практики",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.3).sp
                ),
                color = Color(0xFF2F3A35)
            )
            Text(
                text = "Инструменты для заботы о себе и внутреннего равновесия",
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 23.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF63706B)
            )
        }
    }
}

@Composable
fun PracticeHeroCard(
    card: PracticeCardUi,
    onClick: () -> Unit,
    index: Int,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    LaunchedEffect(Unit) {
        visible = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 520,
            delayMillis = index * 90,
            easing = FastOutSlowInEasing
        ),
        label = "cardAlpha"
    )
    val translateY by animateDpAsState(
        targetValue = if (visible) 0.dp else 22.dp,
        animationSpec = tween(
            durationMillis = 540,
            delayMillis = index * 90,
            easing = FastOutSlowInEasing
        ),
        label = "cardOffsetY"
    )
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = tween(120),
        label = "cardScale"
    )

    val cardShape: Shape = RoundedCornerShape(30.dp)
    val arrowColor = Color(0xFF50595A)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer {
                this.alpha = alpha
                this.translationY = translateY.toPx()
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = 12.dp,
                shape = cardShape,
                ambientColor = Color(0x25000000),
                spotColor = Color(0x18000000)
            )
            .clip(cardShape)
            .background(
                brush = Brush.linearGradient(card.accentColors)
            )
            .border(1.dp, Color.White.copy(alpha = 0.65f), cardShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(18.dp)
    ) {
        AmbientDecorations(card.title)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color.White.copy(alpha = 0.6f))
                    .border(1.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(18.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = card.icon,
                    contentDescription = null,
                    tint = Color(0xFF37434A),
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Color(0xFF2D3738)
                )
                Text(
                    text = card.subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                    color = Color(0xFF536160),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                if (card.badgeText.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White.copy(alpha = 0.72f))
                            .border(1.dp, Color.White.copy(alpha = 0.9f), RoundedCornerShape(999.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = card.badgeText,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF4F4A66)
                        )
                    }
                }
            }
            Text(
                text = "→",
                color = arrowColor,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
private fun AmbientDecorations(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(72.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.18f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(width = 64.dp, height = 22.dp)
                .clip(RoundedCornerShape(999.dp))
                .background(Color.White.copy(alpha = 0.22f))
        )
        val extraColor = when (title) {
            "Дыхание" -> Color(0x5CFFFFFF)
            "Аффирмации" -> Color(0x47FFFFFF)
            else -> Color(0x66FFFFFF)
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(52.dp)
                .clip(CircleShape)
                .background(extraColor)
        )
    }
}

@Composable
fun PracticeFooterCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.horizontalGradient(
                    colors = listOf(Color(0xFFFFF3CD), Color(0xFFFFF8E6))
                )
            )
            .border(1.dp, Color(0xFFF6E7B9), RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "☀",
                fontSize = 18.sp
            )
            Text(
                text = "Ты делаешь важное дело - заботишься о себе",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF6D5D32),
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
