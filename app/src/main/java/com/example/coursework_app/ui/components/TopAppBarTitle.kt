package com.example.coursework_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TopAppBarTitle(
    title: Int,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    showBorder: Boolean = true,
    useGradient: Boolean = false,
    gradientColors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.secondary
    ),
    roundedCornerSize: Int = 30,
    fontSize: Int = 20,
    fontWeight: FontWeight = FontWeight.Normal,
    horizontalPadding: Int = 16,
    verticalPadding: Int = 12
) {
    val modifierWithBackground = if (useGradient) {
        modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding.dp)
            .padding(top = verticalPadding.dp)
            .clip(RoundedCornerShape(roundedCornerSize.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = gradientColors
                )
            )
    } else {
        modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding.dp)
            .padding(top = verticalPadding.dp)
            .clip(RoundedCornerShape(roundedCornerSize.dp))
            .background(containerColor)
            .then(
                if (showBorder) {
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(roundedCornerSize.dp)
                    )
                } else {
                    Modifier
                }
            )
    }

    Box(
        modifier = modifierWithBackground
            .padding(vertical = verticalPadding.dp)
    ) {
        Text(
            text = title,
            color = textColor,
            fontSize = fontSize.sp,
            fontWeight = fontWeight,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
    }
}

