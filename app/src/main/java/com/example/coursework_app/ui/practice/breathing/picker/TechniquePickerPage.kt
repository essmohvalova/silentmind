package com.example.coursework_app.ui.practice.breathing.picker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.coursework_app.ui.practice.BreathingTechnique

@Composable
fun TechniquePickerPage(
    technique: BreathingTechnique,
    selectedCycles: Int,
    palette: TechniquePalette,
    onSelectCycles: (Int) -> Unit,
    onStartClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = technique.rhythmLabel,
            style = MaterialTheme.typography.titleMedium,
            color = palette.accentDark,
            modifier = Modifier.padding(top = 4.dp)
        )
        Text(
            text = technique.description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 12.dp, start = 12.dp, end = 12.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = palette.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.fillMaxWidth().height(220.dp),
                contentAlignment = Alignment.Center
            ) { TechniqueVisual(technique = technique) }
        }
        CycleRow(
            selectedCycles = selectedCycles,
            accentColor = palette.accentDark,
            onSelectCycles = onSelectCycles
        )
        Button(
            onClick = onStartClick,
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = palette.accentDark,
                contentColor = MaterialTheme.colorScheme.surface
            )
        ) { Text("Начать", modifier = Modifier.padding(vertical = 4.dp)) }
    }
}
