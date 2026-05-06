@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.example.coursework_app.ui.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.coursework_app.ui.components.AppBarTitle
import kotlinx.coroutines.launch

@Composable
fun BreathingPracticeScreen(
    onBackClick: () -> Unit,
    onTechniqueClick: (BreathingTechnique, Int) -> Unit,
) {
    BreathingTechniquePickerScreen(
        onBackClick = onBackClick,
        onTechniqueClick = onTechniqueClick
    )
}

@Composable
fun BreathingTechniquePickerScreen(
    onBackClick: () -> Unit,
    onTechniqueClick: (BreathingTechnique, Int) -> Unit,
) {
    val techniques = BreathingTechnique.entries
    val pageCount = Int.MAX_VALUE
    val startPage = pageCount / 2 - (pageCount / 2) % techniques.size
    val pagerState = rememberPagerState(initialPage = startPage) { pageCount }
    val scope = rememberCoroutineScope()
    var selectedCycles by remember { mutableIntStateOf(1) }
    val selectedTechnique = techniques[pagerState.currentPage % techniques.size]
    val palette = selectedTechnique.palette()

    Column(modifier = Modifier.fillMaxSize()) {
        AppBarTitle(
            title = "Дыхание",
            modifier = Modifier.padding(bottom = 12.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) }
                }
            ) {
                Text("◀", color = palette.accentDark, style = MaterialTheme.typography.titleLarge)
            }
            Text(
                text = selectedTechnique.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = palette.accentDark,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                }
            ) {
                Text("▶", color = palette.accentDark, style = MaterialTheme.typography.titleLarge)
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
        ) { page ->
            val technique = techniques[page % techniques.size]
            TechniquePickerPage(
                technique = technique,
                selectedCycles = selectedCycles,
                palette = technique.palette(),
                onSelectCycles = { selectedCycles = it },
                onStartClick = { onTechniqueClick(technique, selectedCycles) },
            )
        }
        TextButton(
            onClick = onBackClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text("Назад", color = palette.accentDark)
        }
    }
}

@Composable
private fun TechniquePickerPage(
    technique: BreathingTechnique,
    selectedCycles: Int,
    palette: TechniquePalette,
    onSelectCycles: (Int) -> Unit,
    onStartClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = technique.rhythmLabel,
            style = MaterialTheme.typography.titleMedium,
            color = palette.accentDark,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = technique.description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(
                containerColor = technique.palette().surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentAlignment = Alignment.Center
            ) {
                TechniqueVisual(technique = technique)
            }
        }
        CycleRow(
            selectedCycles = selectedCycles,
            accentColor = palette.accentDark,
            onSelectCycles = onSelectCycles
        )
        Button(
            onClick = onStartClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = palette.accentDark,
                contentColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Text("Начать", modifier = Modifier.padding(vertical = 4.dp))
        }
    }
}

@Composable
private fun CycleRow(
    selectedCycles: Int,
    accentColor: androidx.compose.ui.graphics.Color,
    onSelectCycles: (Int) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Циклы",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(end = 8.dp)
        )
        (1..5).forEach { cycle ->
            val isSelected = cycle == selectedCycles
            val interactionSource = remember(cycle) { MutableInteractionSource() }
            Box(
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        onClick = { onSelectCycles(cycle) }
                    )
                    .background(
                        color = if (isSelected) accentColor else MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
                    .border(
                        width = if (isSelected) 0.dp else 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cycle.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .background(androidx.compose.ui.graphics.Color.Transparent, CircleShape)
                        .padding(2.dp)
                )
            }
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}

@Composable
private fun TechniqueVisual(
    technique: BreathingTechnique,
) {
    val palette = technique.palette()
    when (technique.visualType) {
        BreathingVisualType.Box -> androidx.compose.foundation.Canvas(modifier = Modifier.size(220.dp)) {
            val stroke = 10f
            val pad = 28f
            val corner = 34f
            val rectSize = size.minDimension - (pad * 2)
            val topLeft = Offset(pad, pad)
            drawRoundRect(
                color = palette.accent.copy(alpha = 0.18f),
                topLeft = topLeft,
                size = Size(rectSize, rectSize),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(corner, corner),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke, pathEffect = PathEffect.cornerPathEffect(20f))
            )
            drawRoundRect(
                brush = Brush.horizontalGradient(listOf(palette.accent, palette.accentDark)),
                topLeft = topLeft,
                size = Size(rectSize, rectSize),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(corner, corner),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke, cap = StrokeCap.Round)
            )
            drawCircle(color = palette.surface, radius = 36f, center = center)
        }
        BreathingVisualType.Circle -> androidx.compose.foundation.Canvas(modifier = Modifier.size(220.dp)) {
            val stroke = 9f
            val inset = 22f
            val diameter = size.minDimension - inset * 2
            val segments = listOf(4f, 7f, 8f)
            var start = -90f
            segments.forEachIndexed { index, part ->
                val sweep = 360f * (part / segments.sum())
                val gap = 14f
                drawArc(
                    color = if (index == 0) palette.accentDark else palette.accent,
                    startAngle = start + gap / 2f,
                    sweepAngle = (sweep - gap).coerceAtLeast(8f),
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = Size(diameter, diameter),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke, cap = StrokeCap.Round)
                )
                start += sweep
            }
            drawCircle(color = palette.surface, radius = 36f, center = center)
        }
        BreathingVisualType.Nostril -> Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
            Box(modifier = Modifier.size(88.dp).background(palette.accentDark.copy(alpha = 0.78f), CircleShape))
            Box(modifier = Modifier.size(88.dp).background(palette.accent.copy(alpha = 0.52f), CircleShape))
        }
    }
    Text(
        text = when (technique.visualType) {
            BreathingVisualType.Box -> "Следуйте\nза циклом"
            BreathingVisualType.Circle -> "Следуйте\nза дугой"
            BreathingVisualType.Nostril -> "Повторяйте\nцикл"
        },
        style = MaterialTheme.typography.labelLarge,
        color = palette.accentDark,
        textAlign = TextAlign.Center
    )
}

private data class TechniquePalette(
    val surface: Color,
    val accent: Color,
    val accentDark: Color,
)

private fun BreathingTechnique.palette(): TechniquePalette =
    when (themeColor) {
        BreathingThemeColor.Blue -> TechniquePalette(
            surface = Color(0xFFEFF4FA),
            accent = Color(0xFFAFC2D6),
            accentDark = Color(0xFF6C819A)
        )
        BreathingThemeColor.Green -> TechniquePalette(
            surface = Color(0xFFEEF5F0),
            accent = Color(0xFFAAC3B3),
            accentDark = Color(0xFF6F8D7C)
        )
        BreathingThemeColor.Purple -> TechniquePalette(
            surface = Color(0xFFF3F0FA),
            accent = Color(0xFFBEB2D8),
            accentDark = Color(0xFF7F729F)
        )
    }
