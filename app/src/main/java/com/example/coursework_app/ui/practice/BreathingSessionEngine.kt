package com.example.coursework_app.ui.practice

enum class BreathPhaseType {
    Inhale, Hold, Exhale, Pause
}

data class BreathingPhaseSpec(
    val type: BreathPhaseType,
    val durationSec: Int,
    val subtitle: String? = null,
) {
    val title: String
        get() = when (type) {
            BreathPhaseType.Inhale -> "Вдох"
            BreathPhaseType.Hold -> "Задержка"
            BreathPhaseType.Exhale -> "Выдох"
            BreathPhaseType.Pause -> "Пауза"
        }
}

data class BreathingSessionFrame(
    val currentPhase: BreathingPhaseSpec,
    val phaseElapsedMs: Long,
    val cycleIndex: Int,
    val totalCycles: Int,
    val elapsedMs: Long,
    val totalDurationMs: Long,
) {
    val remainingSec: Int
        get() = ((totalDurationMs - elapsedMs).coerceAtLeast(0L) / 1000L).toInt()

    val totalProgress: Float
        get() = if (totalDurationMs <= 0L) 0f else elapsedMs.toFloat() / totalDurationMs.toFloat()

    val phaseProgress: Float
        get() {
            val phaseDurationMs = (currentPhase.durationSec * 1000L).coerceAtLeast(1L)
            return phaseElapsedMs.toFloat() / phaseDurationMs.toFloat()
        }
}

fun cycleDurationSec(technique: BreathingTechnique): Int =
    technique.phases.sumOf { it.durationSec }.coerceAtLeast(1)

fun totalDurationSec(technique: BreathingTechnique, selectedCycles: Int): Int =
    cycleDurationSec(technique) * selectedCycles.coerceAtLeast(1)

fun frameAtElapsed(
    technique: BreathingTechnique,
    selectedCycles: Int,
    elapsedMs: Long,
): BreathingSessionFrame {
    val phases = technique.phases.ifEmpty {
        listOf(BreathingPhaseSpec(type = BreathPhaseType.Inhale, durationSec = 1))
    }
    val cycleDurationMs = (phases.sumOf { it.durationSec } * 1000L).coerceAtLeast(1L)
    val totalDurationMs = totalDurationSec(technique, selectedCycles) * 1000L
    val safeElapsedMs = elapsedMs.coerceIn(0L, totalDurationMs)
    var msInCycle = safeElapsedMs % cycleDurationMs
    var active = phases.first()
    for (phase in phases) {
        val phaseDurationMs = phase.durationSec * 1000L
        if (msInCycle < phaseDurationMs) {
            active = phase
            break
        }
        msInCycle -= phaseDurationMs
    }
    val currentCycle = ((safeElapsedMs / cycleDurationMs) + 1).toInt().coerceAtMost(selectedCycles.coerceAtLeast(1))
    return BreathingSessionFrame(
        currentPhase = active,
        phaseElapsedMs = msInCycle,
        cycleIndex = currentCycle,
        totalCycles = selectedCycles.coerceAtLeast(1),
        elapsedMs = safeElapsedMs,
        totalDurationMs = totalDurationMs,
    )
}

fun Int.asMmSs(): String {
    val safe = coerceAtLeast(0)
    return "%02d:%02d".format(safe / 60, safe % 60)
}
