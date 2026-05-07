package com.example.coursework_app.ui.practice.breathing.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.example.coursework_app.ui.practice.BreathPhaseType
import com.example.coursework_app.ui.practice.BreathingPhaseSpec

@Composable
fun NostrilSessionDiagram(
    currentPhase: BreathingPhaseSpec?,
    phaseProgress: Float,
    palette: SessionPalette,
    centerText: String,
) {
    val p = phaseProgress.coerceIn(0f, 1f)
    val (leftScale, rightScale) = when (currentPhase?.type) {
        BreathPhaseType.Inhale -> 0.86f + (0.30f * p) to 1.16f - (0.34f * p)
        BreathPhaseType.Exhale -> 1.16f - (0.34f * p) to 0.86f + (0.30f * p)
        else -> 0.86f to 0.86f
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
