package com.example.coursework_app.ui.practice.breathing.picker

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.coursework_app.ui.practice.BreathingTechnique
import com.example.coursework_app.ui.practice.BreathingVisualType

@Composable
fun TechniqueVisual(technique: BreathingTechnique) {
    val palette = technique.palette()
    when (technique.visualType) {
        BreathingVisualType.Box -> Canvas(modifier = Modifier.size(220.dp)) {
            val pad = 28f
            val rectSize = size.minDimension - (pad * 2)
            val topLeft = Offset(pad, pad)
            drawRoundRect(
                color = palette.accent.copy(alpha = 0.18f),
                topLeft = topLeft,
                size = Size(rectSize, rectSize),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(34f, 34f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 10f, pathEffect = PathEffect.cornerPathEffect(20f))
            )
            drawRoundRect(
                brush = Brush.horizontalGradient(listOf(palette.accent, palette.accentDark)),
                topLeft = topLeft,
                size = Size(rectSize, rectSize),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(34f, 34f),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 10f, cap = StrokeCap.Round)
            )
            drawCircle(color = palette.surface, radius = 36f, center = center)
        }
        BreathingVisualType.Circle -> Canvas(modifier = Modifier.size(220.dp)) {
            val inset = 22f
            val diameter = size.minDimension - inset * 2
            var start = -90f
            listOf(4f, 7f, 8f).forEachIndexed { index, part ->
                val sweep = 360f * (part / 19f)
                val gap = 14f
                drawArc(
                    color = if (index == 0) palette.accentDark else palette.accent,
                    startAngle = start + gap / 2f,
                    sweepAngle = (sweep - gap).coerceAtLeast(8f),
                    useCenter = false,
                    topLeft = Offset(inset, inset),
                    size = Size(diameter, diameter),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 9f, cap = StrokeCap.Round)
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
