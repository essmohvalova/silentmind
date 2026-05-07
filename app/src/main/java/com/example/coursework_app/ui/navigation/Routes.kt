package com.example.coursework_app.ui.navigation

object Routes {

    // root
    const val WELCOME = "welcome"
    const val ONBOARDING = "onboarding"
    const val BOTTOM = "bottom"


    // bottom tabs
    const val BOTTOM_MAIN = "main"
    const val BOTTOM_JOURNAL = "journal"
    const val EMOTION_GRAPH = "emotion_graph"
    const val PRACTICE_GRAPH = "practice_graph"

    // emotion flow
    const val EMOTION_MAIN = "emotion_main"
    const val EMOTION_COMPLETE = "emotion_complete"

    const val EMOTION_DETAILS = "emotion_details"

    // practice flow
    const val PRACTICE_MAIN = "practice_main"
    const val BREATHING_PRACTICE = "breathing_practice"
    const val BREATHING_COMPLETE = "breathing_complete"
    const val BREATHING_TECHNIQUE_ARG = "techniqueId"
    const val BREATHING_CYCLES_ARG = "cycles"
    const val BREATHING_TECHNIQUE =
        "breathing_technique/{$BREATHING_TECHNIQUE_ARG}/{$BREATHING_CYCLES_ARG}"
    const val AFFIRMATION_PRACTICE = "affirmation_practice"
    const val RANDOM_PRACTICE = "random_practice"

    fun breathingTechniqueRoute(techniqueId: String, cycles: Int): String {
        return "breathing_technique/$techniqueId/$cycles"
    }
}
