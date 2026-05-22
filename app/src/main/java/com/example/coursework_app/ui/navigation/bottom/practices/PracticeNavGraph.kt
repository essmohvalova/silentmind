package com.example.coursework_app.ui.navigation.bottom.practices

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.navArgument
import com.example.coursework_app.ui.navigation.Routes
import com.example.coursework_app.ui.practice.AffirmationPracticeScreen
import com.example.coursework_app.ui.practice.BreathingCompleteScreen
import com.example.coursework_app.ui.practice.BreathingPracticeScreen
import com.example.coursework_app.ui.practice.BreathingTechnique
import com.example.coursework_app.ui.practice.BreathingTechniqueScreen
import com.example.coursework_app.ui.practice.PracticeMainScreen
import com.example.coursework_app.ui.practice.RandomPracticeScreen

fun NavGraphBuilder.practiceNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Routes.PRACTICE_MAIN,
        route = Routes.PRACTICE_GRAPH
    ) {
        composable(Routes.PRACTICE_MAIN) {
            PracticeMainScreen(
                onBreathingClick = { navController.navigate(Routes.BREATHING_PRACTICE) },
                onAffirmationClick = { navController.navigate(Routes.AFFIRMATION_PRACTICE) },
                onRandomPracticeClick = { navController.navigate(Routes.RANDOM_PRACTICE) }
            )
        }

        composable(Routes.BREATHING_PRACTICE) {
            BreathingPracticeScreen(
                onBackClick = navController::popBackStack,
                onTechniqueClick = { technique, cycles ->
                    navController.navigate(Routes.breathingTechniqueRoute(technique.id, cycles))
                }
            )
        }

        composable(
            route = Routes.BREATHING_TECHNIQUE,
            arguments = listOf(
                navArgument(Routes.BREATHING_TECHNIQUE_ARG) {
                    type = NavType.StringType
                },
                navArgument(Routes.BREATHING_CYCLES_ARG) {
                    type = NavType.IntType
                }
            )
        ) { entry ->
            val techniqueId = entry.arguments?.getString(Routes.BREATHING_TECHNIQUE_ARG)
            val cycles = entry.arguments?.getInt(Routes.BREATHING_CYCLES_ARG) ?: 1
            val technique = BreathingTechnique.fromId(techniqueId)

            if (technique != null) {
                BreathingTechniqueScreen(
                    technique = technique,
                    selectedCycles = cycles,
                    onSessionCompleted = { navController.navigate(Routes.BREATHING_COMPLETE) },
                    onBackClick = navController::popBackStack
                )
            } else {
                BreathingPracticeScreen(
                    onBackClick = navController::popBackStack,
                    onTechniqueClick = { selected, selectedCycles ->
                        navController.navigate(Routes.breathingTechniqueRoute(selected.id, selectedCycles))
                    }
                )
            }
        }

        composable(Routes.BREATHING_COMPLETE) {
            BreathingCompleteScreen(
                onContinueClick = {
                    navController.navigate(Routes.BOTTOM_MAIN) {
                        popUpTo(Routes.PRACTICE_GRAPH) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.AFFIRMATION_PRACTICE) {
            AffirmationPracticeScreen(onBackClick = navController::popBackStack)
        }

        composable(Routes.RANDOM_PRACTICE) {
            RandomPracticeScreen(onBackClick = navController::popBackStack)
        }
    }
}
