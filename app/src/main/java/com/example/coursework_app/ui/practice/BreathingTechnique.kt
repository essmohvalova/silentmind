package com.example.coursework_app.ui.practice

enum class BreathingTechnique(
    val id: String,
    val title: String,
    val rhythmLabel: String,
    val description: String,
    val phases: List<BreathingPhaseSpec>,
    val themeColor: BreathingThemeColor,
    val visualType: BreathingVisualType,
) {
    BoxBreathing(
        id = "box",
        title = "Квадратное дыхание",
        rhythmLabel = "4-4-4-4",
        description = "Простая техника для снятия стресса и восстановления баланса.",
        phases = listOf(
            BreathingPhaseSpec(BreathPhaseType.Inhale, 4),
            BreathingPhaseSpec(BreathPhaseType.Hold, 4),
            BreathingPhaseSpec(BreathPhaseType.Exhale, 4),
            BreathingPhaseSpec(BreathPhaseType.Pause, 4),
        ),
        themeColor = BreathingThemeColor.Blue,
        visualType = BreathingVisualType.Box
    ),
    FourSevenEight(
        id = "four_seven_eight",
        title = "Техника 4-7-8",
        rhythmLabel = "4-7-8",
        description = "Эффективная техника для расслабления и мягкой подготовки ко сну.",
        phases = listOf(
            BreathingPhaseSpec(BreathPhaseType.Inhale, 4),
            BreathingPhaseSpec(BreathPhaseType.Hold, 7),
            BreathingPhaseSpec(BreathPhaseType.Exhale, 8),
        ),
        themeColor = BreathingThemeColor.Green,
        visualType = BreathingVisualType.Circle
    ),
    AlternateNostril(
        id = "alternate_nostril",
        title = "Попеременное дыхание",
        rhythmLabel = "4-4-4-4",
        description = "Балансирует нервную систему и улучшает концентрацию.",
        phases = listOf(
            BreathingPhaseSpec(BreathPhaseType.Inhale, 4, "Левая ноздря"),
            BreathingPhaseSpec(BreathPhaseType.Exhale, 4, "Правая ноздря"),
            BreathingPhaseSpec(BreathPhaseType.Inhale, 4, "Правая ноздря"),
            BreathingPhaseSpec(BreathPhaseType.Exhale, 4, "Левая ноздря"),
        ),
        themeColor = BreathingThemeColor.Purple,
        visualType = BreathingVisualType.Nostril
    );

    companion object {
        fun fromId(id: String?): BreathingTechnique? {
            return entries.firstOrNull { it.id == id }
        }
    }
}

enum class BreathingThemeColor {
    Blue, Green, Purple
}

enum class BreathingVisualType {
    Box, Circle, Nostril
}
