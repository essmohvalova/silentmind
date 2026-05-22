package com.example.coursework_app.ui.practice

import androidx.compose.runtime.Composable
import com.example.coursework_app.ui.practice.breathing.session.BreathingPracticeSessionScreen

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
