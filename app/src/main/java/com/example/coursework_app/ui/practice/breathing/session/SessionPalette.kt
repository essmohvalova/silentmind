package com.example.coursework_app.ui.practice.breathing.session

import androidx.compose.ui.graphics.Color
import com.example.coursework_app.ui.practice.BreathingTechnique
import com.example.coursework_app.ui.practice.BreathingThemeColor

data class SessionPalette(
    val surface: Color,
    val accent: Color,
    val accentDark: Color,
)

fun BreathingTechnique.sessionPalette(): SessionPalette =
    when (themeColor) {
        BreathingThemeColor.Blue -> SessionPalette(Color(0xFFEFF4FA), Color(0xFFAFC2D6), Color(0xFF6C819A))
        BreathingThemeColor.Green -> SessionPalette(Color(0xFFEEF5F0), Color(0xFFAAC3B3), Color(0xFF6F8D7C))
        BreathingThemeColor.Purple -> SessionPalette(Color(0xFFF3F0FA), Color(0xFFBEB2D8), Color(0xFF7F729F))
    }
