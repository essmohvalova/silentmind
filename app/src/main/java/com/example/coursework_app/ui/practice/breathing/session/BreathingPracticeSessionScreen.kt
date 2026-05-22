package com.example.coursework_app.ui.practice.breathing.session

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.coursework_app.ui.practice.BreathingTechnique
import com.example.coursework_app.ui.practice.BreathingVisualType
import com.example.coursework_app.ui.practice.asMmSs
import com.example.coursework_app.ui.practice.frameAtElapsed
import com.example.coursework_app.ui.practice.totalDurationSec
import kotlinx.coroutines.isActive

@Composable
fun BreathingPracticeSessionScreen(
    technique: BreathingTechnique,
    selectedCycles: Int,
    onSessionCompleted: () -> Unit = {},
    onBackClick: () -> Unit,
) {
    var elapsedMs by remember { mutableLongStateOf(0L) }
    var paused by remember { androidx.compose.runtime.mutableStateOf(false) }
    var finished by remember { androidx.compose.runtime.mutableStateOf(false) }
    val totalDurationMs = remember(technique, selectedCycles) { totalDurationSec(technique, selectedCycles) * 1000L }
    val frame = remember(technique, selectedCycles, elapsedMs) { frameAtElapsed(technique, selectedCycles, elapsedMs) }
    val palette = remember(technique) { technique.sessionPalette() }
    LaunchedEffect(paused, finished, totalDurationMs) {
        if (paused || finished) return@LaunchedEffect
        var last = 0L
        while (isActive && !paused && !finished && elapsedMs < totalDurationMs) {
            withFrameNanos { now -> if (last != 0L) elapsedMs = (elapsedMs + (now - last) / 1_000_000L).coerceAtMost(totalDurationMs); last = now }
        }
        if (elapsedMs >= totalDurationMs) finished = true
    }
    LaunchedEffect(finished) { if (finished) onSessionCompleted() }
    val activePhase = if (finished) null else frame.currentPhase

    Column(
        modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Brush.verticalGradient(
            listOf(palette.surface, MaterialTheme.colorScheme.background, palette.surface.copy(alpha = 0.7f))
        )).navigationBarsPadding()
    ) {
        Text(technique.title, style = MaterialTheme.typography.headlineSmall, color = palette.accentDark, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(top = 18.dp, bottom = 8.dp))
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            if (technique.visualType == BreathingVisualType.Nostril) {
                Text("Вдыхай по очереди левой и правой ноздрей", style = MaterialTheme.typography.bodyMedium, color = palette.accentDark, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp))
            }
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                SessionTechniqueVisual(technique, activePhase, frame.phaseProgress, palette, if (finished) "Практика\nзавершена" else frame.currentPhase.title)
            }
            LinearProgressIndicator(
                progress = { frame.totalProgress.coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(999.dp)),
                color = palette.accentDark,
                trackColor = palette.accent.copy(alpha = 0.25f)
            )
            Text("Цикл ${frame.cycleIndex} из ${frame.totalCycles}  •  Осталось ${frame.remainingSec.asMmSs()}", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f), modifier = Modifier.alpha(if (finished) 0.74f else 1f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = { paused = !paused }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant, contentColor = MaterialTheme.colorScheme.onSurface)) {
                    Text(if (paused) "Продолжить" else "Пауза")
                }
                Button(onClick = onBackClick, modifier = Modifier.weight(1f), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = palette.accentDark, contentColor = MaterialTheme.colorScheme.surface)) {
                    Text(if (finished) "Готово" else "Завершить")
                }
            }
        }
    }
}
