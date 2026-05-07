package com.example.coursework_app.ui.practice.breathing.session

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.coursework_app.ui.practice.BreathPhaseType
import com.example.coursework_app.ui.practice.BreathingPhaseSpec

@Composable
fun CircleSessionDiagram(
    currentPhase: BreathingPhaseSpec?,
    phaseProgress: Float,
    palette: SessionPalette,
    centerText: String,
) {
    val progress = phaseProgress.coerceIn(0f, 1f)
    val scale = phaseScale(currentPhase?.type ?: BreathPhaseType.Pause, progress)
    val activeIndex = when (currentPhase?.type) {
        BreathPhaseType.Inhale -> 0
        BreathPhaseType.Hold -> 1
        BreathPhaseType.Exhale -> 2
        else -> -1
    }
    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(250.dp).scale(scale)) {
            val stroke = 11f; val inset = 30f; val d = size.minDimension - inset * 2
            val segments = listOf(4f, 7f, 8f)
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
