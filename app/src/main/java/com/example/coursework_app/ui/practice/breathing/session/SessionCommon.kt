package com.example.coursework_app.ui.practice.breathing.session

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.coursework_app.ui.practice.BreathPhaseType

fun phaseScale(type: BreathPhaseType, progress: Float): Float {
    val p = progress.coerceIn(0f, 1f)
    return when (type) {
        BreathPhaseType.Inhale -> 0.82f + (0.30f * p)
        BreathPhaseType.Hold -> 1.12f
        BreathPhaseType.Exhale -> 1.12f - (0.30f * p)
        BreathPhaseType.Pause -> 0.82f
    }
}

@Composable
fun CenterLabel(text: String, palette: SessionPalette) {
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
