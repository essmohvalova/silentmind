package com.example.coursework_app.ui.practice.breathing.session

import androidx.compose.runtime.Composable
import com.example.coursework_app.ui.practice.BreathingPhaseSpec
import com.example.coursework_app.ui.practice.BreathingTechnique
import com.example.coursework_app.ui.practice.BreathingVisualType

@Composable
fun SessionTechniqueVisual(
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
