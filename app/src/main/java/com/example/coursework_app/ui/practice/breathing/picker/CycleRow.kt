package com.example.coursework_app.ui.practice.breathing.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CycleRow(
    selectedCycles: Int,
    accentColor: Color,
    onSelectCycles: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Циклы", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(end = 8.dp))
        (1..5).forEach { cycle ->
            val isSelected = cycle == selectedCycles
            val interactionSource = remember(cycle) { MutableInteractionSource() }
            androidx.compose.foundation.layout.Box(
                modifier = Modifier.padding(horizontal = 6.dp).size(28.dp).clip(CircleShape)
                    .clickable(interactionSource = interactionSource, indication = null) { onSelectCycles(cycle) }
                    .background(if (isSelected) accentColor else MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                    .border(
                        width = if (isSelected) 0.dp else 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cycle.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                )
            }
            Spacer(modifier = Modifier.size(2.dp))
        }
    }
}
