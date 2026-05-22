package com.example.coursework_app.ui.practice.breathing.picker

import androidx.compose.ui.graphics.Color
import com.example.coursework_app.ui.practice.BreathingTechnique
import com.example.coursework_app.ui.practice.BreathingThemeColor

data class TechniquePalette(
    val surface: Color,
    val accent: Color,
    val accentDark: Color,
)

fun BreathingTechnique.palette(): TechniquePalette =
    when (themeColor) {
        BreathingThemeColor.Blue -> TechniquePalette(
            surface = Color(0xFFEFF4FA),
            accent = Color(0xFFAFC2D6),
            accentDark = Color(0xFF6C819A)
        )
        BreathingThemeColor.Green -> TechniquePalette(
            surface = Color(0xFFEEF5F0),
            accent = Color(0xFFAAC3B3),
            accentDark = Color(0xFF6F8D7C)
        )
        BreathingThemeColor.Purple -> TechniquePalette(
            surface = Color(0xFFF3F0FA),
            accent = Color(0xFFBEB2D8),
            accentDark = Color(0xFF7F729F)
        )
    }
