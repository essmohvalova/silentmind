package com.example.coursework_app.ui.practice

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive

@Composable
fun BreathingTechniqueScreen(
    technique: BreathingTechnique,
    selectedCycles: Int,
    onSessionCompleted: () -> Unit = {},
    onBackClick: () -> Unit,
) {
    BreathingPracticeSessionScreen(
        technique = technique,
        selectedCycles = selectedCycles,
        onSessionCompleted = onSessionCompleted,
        onBackClick = onBackClick
    )
}

@Composable
fun BreathingPracticeSessionScreen(
    technique: BreathingTechnique,
    selectedCycles: Int,
    onSessionCompleted: () -> Unit = {},
    onBackClick: () -> Unit,
) {
    var elapsedMs by remember { mutableLongStateOf(0L) }
    var paused by remember { androidx.compose.runtime.mutableStateOf(false) }
    val totalDurationMs = remember(technique, selectedCycles) {
        totalDurationSec(technique, selectedCycles) * 1000L
    }
    val frame = remember(technique, selectedCycles, elapsedMs) {
        frameAtElapsed(technique, selectedCycles, elapsedMs)
    }
    var finished by remember { androidx.compose.runtime.mutableStateOf(false) }
    val activePhase = if (finished) null else frame.currentPhase
    val palette = remember(technique) { technique.sessionPalette() }
    val contentAlpha = if (finished) 0.74f else 1f

    LaunchedEffect(paused, finished, totalDurationMs) {
        if (paused || finished) return@LaunchedEffect
        var lastFrameTimeNs = 0L
        while (isActive && !paused && !finished && elapsedMs < totalDurationMs) {
            withFrameNanos { nowNs ->
                if (lastFrameTimeNs == 0L) {
                    lastFrameTimeNs = nowNs
                } else {
                    val deltaMs = (nowNs - lastFrameTimeNs) / 1_000_000L
                    if (deltaMs > 0L) {
                        elapsedMs = (elapsedMs + deltaMs).coerceAtMost(totalDurationMs)
                        lastFrameTimeNs = nowNs
                    }
                }
            }
        }
        if (elapsedMs >= totalDurationMs) finished = true
    }
    LaunchedEffect(finished) {
        if (finished) onSessionCompleted()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        palette.surface,
                        MaterialTheme.colorScheme.background,
                        palette.surface.copy(alpha = 0.7f)
                    )
                )
            )
            .navigationBarsPadding()
    ) {
        Text(
            text = technique.title,
            style = MaterialTheme.typography.headlineSmall,
            color = palette.accentDark,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp, bottom = 8.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            if (technique.visualType == BreathingVisualType.Nostril) {
                Text(
                    text = "Вдыхай по очереди левой и правой ноздрей",
                    style = MaterialTheme.typography.bodyMedium,
                    color = palette.accentDark,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                SessionTechniqueVisual(
                    technique = technique,
                    currentPhase = activePhase,
                    phaseProgress = frame.phaseProgress,
                    palette = palette,
                    centerText = if (finished) "Практика\nзавершена" else frame.currentPhase.title
                )
            }

            LinearProgressIndicator(
                progress = { frame.totalProgress.coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(999.dp)),
                color = palette.accentDark,
                trackColor = palette.accent.copy(alpha = 0.25f),
            )
            Text(
                text = "Цикл ${frame.cycleIndex} из ${frame.totalCycles}  •  Осталось ${frame.remainingSec.asMmSs()}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                modifier = Modifier.alpha(contentAlpha)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { paused = !paused },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) { Text(if (paused) "Продолжить" else "Пауза") }
                Button(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = palette.accentDark,
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(if (finished) "Готово" else "Завершить")
                }
            }
        }
    }
}

private data class SessionPalette(
    val surface: Color,
    val accent: Color,
    val accentDark: Color,
)

private fun BreathingTechnique.sessionPalette(): SessionPalette =
    when (themeColor) {
        BreathingThemeColor.Blue -> SessionPalette(
            surface = Color(0xFFEFF4FA),
            accent = Color(0xFFAFC2D6),
            accentDark = Color(0xFF6C819A)
        )
        BreathingThemeColor.Green -> SessionPalette(
            surface = Color(0xFFEEF5F0),
            accent = Color(0xFFAAC3B3),
            accentDark = Color(0xFF6F8D7C)
        )
        BreathingThemeColor.Purple -> SessionPalette(
            surface = Color(0xFFF3F0FA),
            accent = Color(0xFFBEB2D8),
            accentDark = Color(0xFF7F729F)
        )
    }

@Composable
private fun SessionTechniqueVisual(
    technique: BreathingTechnique,
    currentPhase: BreathingPhaseSpec?,
    phaseProgress: Float,
    palette: SessionPalette,
    centerText: String,
) {
    when (technique.visualType) {
        BreathingVisualType.Box -> BoxSessionDiagram(currentPhase, phaseProgress, palette, centerText)
        BreathingVisualType.Circle -> CircleSessionDiagram(currentPhase, phaseProgress, palette, centerText)
        BreathingVisualType.Nostril -> NostrilSessionDiagram(currentPhase, phaseProgress, palette, centerText)
    }
}

@Composable
private fun BoxSessionDiagram(
    currentPhase: BreathingPhaseSpec?,
    phaseProgress: Float,
    palette: SessionPalette,
    centerText: String,
) {
    val progress = phaseProgress.coerceIn(0f, 1f)
    val scale = phaseScale(currentPhase?.type ?: BreathPhaseType.Pause, progress)
    val activeSide = when (currentPhase?.type) {
        BreathPhaseType.Inhale -> 0
        BreathPhaseType.Hold -> 1
        BreathPhaseType.Exhale -> 2
        BreathPhaseType.Pause -> 3
        null -> -1
    }
    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(250.dp).scale(scale)) {
            val pad = 34f
            val side = size.minDimension - pad * 2
            val stroke = 12f
            val topLeft = androidx.compose.ui.geometry.Offset(pad, pad)
            val x = topLeft.x
            val y = topLeft.y
            val segmentGap = 22f
            val inactive = palette.accent.copy(alpha = 0.24f)
            drawLine(inactive, androidx.compose.ui.geometry.Offset(x + segmentGap, y), androidx.compose.ui.geometry.Offset(x + side - segmentGap, y), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(inactive, androidx.compose.ui.geometry.Offset(x + side, y + segmentGap), androidx.compose.ui.geometry.Offset(x + side, y + side - segmentGap), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(inactive, androidx.compose.ui.geometry.Offset(x + side - segmentGap, y + side), androidx.compose.ui.geometry.Offset(x + segmentGap, y + side), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(inactive, androidx.compose.ui.geometry.Offset(x, y + side - segmentGap), androidx.compose.ui.geometry.Offset(x, y + segmentGap), strokeWidth = stroke, cap = StrokeCap.Round)
            if (activeSide >= 0) {
                val segColor = palette.accentDark.copy(alpha = 0.9f)
                when (activeSide) {
                    0 -> drawLine(segColor, androidx.compose.ui.geometry.Offset(x + segmentGap, y), androidx.compose.ui.geometry.Offset(x + segmentGap + (side - segmentGap * 2) * progress, y), strokeWidth = stroke + 2f, cap = StrokeCap.Round)
                    1 -> drawLine(segColor, androidx.compose.ui.geometry.Offset(x + side, y + segmentGap), androidx.compose.ui.geometry.Offset(x + side, y + segmentGap + (side - segmentGap * 2) * progress), strokeWidth = stroke + 2f, cap = StrokeCap.Round)
                    2 -> drawLine(segColor, androidx.compose.ui.geometry.Offset(x + side - segmentGap, y + side), androidx.compose.ui.geometry.Offset(x + side - segmentGap - (side - segmentGap * 2) * progress, y + side), strokeWidth = stroke + 2f, cap = StrokeCap.Round)
                    3 -> drawLine(segColor, androidx.compose.ui.geometry.Offset(x, y + side - segmentGap), androidx.compose.ui.geometry.Offset(x, y + side - segmentGap - (side - segmentGap * 2) * progress), strokeWidth = stroke + 2f, cap = StrokeCap.Round)
                }
                val moving = when (activeSide) {
                    0 -> androidx.compose.ui.geometry.Offset(x + segmentGap + (side - segmentGap * 2) * progress, y)
                    1 -> androidx.compose.ui.geometry.Offset(x + side, y + segmentGap + (side - segmentGap * 2) * progress)
                    2 -> androidx.compose.ui.geometry.Offset(x + side - segmentGap - (side - segmentGap * 2) * progress, y + side)
                    else -> androidx.compose.ui.geometry.Offset(x, y + side - segmentGap - (side - segmentGap * 2) * progress)
                }
                val movingNumber = activeSide + 1
                drawCircle(color = palette.accentDark, radius = 17f, center = moving)
                drawContext.canvas.nativeCanvas.drawText(
                    movingNumber.toString(),
                    moving.x - 5f,
                    moving.y + 6f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.WHITE
                        textSize = 21f
                        isFakeBoldText = true
                    }
                )
            }
            val points = listOf(
                androidx.compose.ui.geometry.Offset(topLeft.x + side / 2, topLeft.y),
                androidx.compose.ui.geometry.Offset(topLeft.x + side, topLeft.y + side / 2),
                androidx.compose.ui.geometry.Offset(topLeft.x + side / 2, topLeft.y + side),
                androidx.compose.ui.geometry.Offset(topLeft.x, topLeft.y + side / 2),
            )
            points.forEachIndexed { index, p ->
                drawCircle(
                    color = if (index == activeSide) palette.accentDark else palette.accent.copy(alpha = 0.55f),
                    radius = if (index == activeSide) 11f else 8f,
                    center = p
                )
            }
        }
        CenterLabel(centerText, palette)
    }
}

@Composable
private fun CircleSessionDiagram(
    currentPhase: BreathingPhaseSpec?,
    phaseProgress: Float,
    palette: SessionPalette,
    centerText: String,
) {
    val progress = phaseProgress.coerceIn(0f, 1f)
    val scale = phaseScale(currentPhase?.type ?: BreathPhaseType.Pause, progress)
    val segments = listOf(4f, 7f, 8f)
    val activeIndex = when (currentPhase?.type) {
        BreathPhaseType.Inhale -> 0
        BreathPhaseType.Hold -> 1
        BreathPhaseType.Exhale -> 2
        else -> -1
    }
    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(250.dp).scale(scale)) {
            val stroke = 11f
            val inset = 30f
            val d = size.minDimension - inset * 2
            var start = -90f
            segments.forEachIndexed { index, part ->
                val sweep = 360f * (part / segments.sum())
                val gap = 14f
                val segmentStart = start + gap / 2f
                val segmentSweep = (sweep - gap).coerceAtLeast(6f)
                val baseColor = if (index == activeIndex) palette.accentDark.copy(alpha = 0.45f) else palette.accent.copy(alpha = 0.55f)
                drawArc(
                    color = baseColor,
                    startAngle = segmentStart,
                    sweepAngle = segmentSweep,
                    useCenter = false,
                    topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                    size = androidx.compose.ui.geometry.Size(d, d),
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
                if (index == activeIndex) {
                    drawArc(
                        color = palette.accentDark,
                        startAngle = segmentStart,
                        sweepAngle = segmentSweep * progress,
                        useCenter = false,
                        topLeft = androidx.compose.ui.geometry.Offset(inset, inset),
                        size = androidx.compose.ui.geometry.Size(d, d),
                        style = Stroke(width = stroke + 2f, cap = StrokeCap.Round)
                    )
                }
                start += sweep
            }
        }
        CenterLabel(centerText, palette)
    }
}

@Composable
private fun NostrilSessionDiagram(
    currentPhase: BreathingPhaseSpec?,
    phaseProgress: Float,
    palette: SessionPalette,
    centerText: String,
) {
    val p = phaseProgress.coerceIn(0f, 1f)
    val leftScale: Float
    val rightScale: Float
    when (currentPhase?.type) {
        BreathPhaseType.Inhale -> {
            leftScale = 0.86f + (0.30f * p)
            rightScale = 1.16f - (0.34f * p)
        }
        BreathPhaseType.Exhale -> {
            leftScale = 1.16f - (0.34f * p)
            rightScale = 0.86f + (0.30f * p)
        }
        else -> {
            leftScale = 0.86f
            rightScale = 0.86f
        }
    }
    Box(contentAlignment = Alignment.Center) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.size(240.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .scale(leftScale)
                    .background(palette.accentDark.copy(alpha = 0.78f), CircleShape),
            )
            Box(
                modifier = Modifier
                    .size(104.dp)
                    .scale(rightScale)
                    .background(palette.accent.copy(alpha = 0.60f), CircleShape),
            )
        }
        CenterLabel(centerText, palette)
    }
}

@Composable
private fun CenterLabel(text: String, palette: SessionPalette) {
    Box(
        modifier = Modifier
            .size(92.dp)
            .background(color = palette.surface.copy(alpha = 0.96f), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = palette.accentDark,
            textAlign = TextAlign.Center
        )
    }
}

private fun phaseScale(type: BreathPhaseType, progress: Float): Float {
    val p = progress.coerceIn(0f, 1f)
    return when (type) {
        BreathPhaseType.Inhale -> 0.82f + (0.30f * p)
        BreathPhaseType.Hold -> 1.12f
        BreathPhaseType.Exhale -> 1.12f - (0.30f * p)
        BreathPhaseType.Pause -> 0.82f
    }
}
