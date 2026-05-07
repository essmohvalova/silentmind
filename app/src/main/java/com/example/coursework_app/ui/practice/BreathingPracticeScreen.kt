package com.example.coursework_app.ui.practice

import androidx.compose.runtime.Composable
import com.example.coursework_app.ui.practice.breathing.picker.BreathingTechniquePickerScreen

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
