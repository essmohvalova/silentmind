package com.example.coursework_app.ui.practice.breathing.session

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.example.coursework_app.ui.practice.BreathPhaseType
import com.example.coursework_app.ui.practice.BreathingPhaseSpec

@Composable
fun BoxSessionDiagram(
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
            val pad = 34f; val side = size.minDimension - pad * 2; val stroke = 12f
            val x = pad; val y = pad; val gap = 22f
            val inactive = palette.accent.copy(alpha = 0.24f)
            drawLine(inactive, androidx.compose.ui.geometry.Offset(x + gap, y), androidx.compose.ui.geometry.Offset(x + side - gap, y), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(inactive, androidx.compose.ui.geometry.Offset(x + side, y + gap), androidx.compose.ui.geometry.Offset(x + side, y + side - gap), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(inactive, androidx.compose.ui.geometry.Offset(x + side - gap, y + side), androidx.compose.ui.geometry.Offset(x + gap, y + side), strokeWidth = stroke, cap = StrokeCap.Round)
            drawLine(inactive, androidx.compose.ui.geometry.Offset(x, y + side - gap), androidx.compose.ui.geometry.Offset(x, y + gap), strokeWidth = stroke, cap = StrokeCap.Round)
            if (activeSide >= 0) {
                val segColor = palette.accentDark.copy(alpha = 0.9f)
                when (activeSide) {
                    0 -> drawLine(segColor, androidx.compose.ui.geometry.Offset(x + gap, y), androidx.compose.ui.geometry.Offset(x + gap + (side - gap * 2) * progress, y), strokeWidth = stroke + 2f, cap = StrokeCap.Round)
                    1 -> drawLine(segColor, androidx.compose.ui.geometry.Offset(x + side, y + gap), androidx.compose.ui.geometry.Offset(x + side, y + gap + (side - gap * 2) * progress), strokeWidth = stroke + 2f, cap = StrokeCap.Round)
                    2 -> drawLine(segColor, androidx.compose.ui.geometry.Offset(x + side - gap, y + side), androidx.compose.ui.geometry.Offset(x + side - gap - (side - gap * 2) * progress, y + side), strokeWidth = stroke + 2f, cap = StrokeCap.Round)
                    3 -> drawLine(segColor, androidx.compose.ui.geometry.Offset(x, y + side - gap), androidx.compose.ui.geometry.Offset(x, y + side - gap - (side - gap * 2) * progress), strokeWidth = stroke + 2f, cap = StrokeCap.Round)
                }
            }
        }
        CenterLabel(centerText, palette)
    }
}
