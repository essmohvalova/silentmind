package com.example.coursework_app.ui.onboarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.unit.dp
import com.example.coursework_app.ui.theme.backgroundDark
import com.example.coursework_app.ui.theme.backgroundLight

@Composable
fun OnboardingIndicator(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalPages) { index ->
            Box(
                modifier = Modifier
                    .width(if (index == currentPage) 24.dp else 8.dp)
                    .height(8.dp)
                    .drawBehind {
                        drawRoundRect(
                            color = if (index == currentPage)
                                backgroundLight
                            else
                                backgroundDark,
                            cornerRadius = CornerRadius(4.dp.toPx())
                        )
                    }
            )

            if (index < totalPages - 1) {
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
    }
}