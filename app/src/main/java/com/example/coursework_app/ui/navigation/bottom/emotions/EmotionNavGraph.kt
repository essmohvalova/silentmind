package com.example.coursework_app.ui.navigation.bottom.emotions

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.remember
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.coursework_app.ui.navigation.Routes
import com.example.coursework_app.ui.emotion.EmotionViewModel

fun NavGraphBuilder.emotionNavGraph(
    navController: NavHostController,
) {
    navigation(
        startDestination = Routes.EMOTION_MAIN,
        route = Routes.EMOTION_GRAPH,
    ) {
        composable(Routes.EMOTION_MAIN) { entry ->
            val parentEntry: NavBackStackEntry? = remember(entry) {
                runCatching { navController.getBackStackEntry(Routes.EMOTION_GRAPH) }.getOrNull()
            }
            val viewModel: EmotionViewModel = if (parentEntry != null) {
                hiltViewModel<EmotionViewModel>(parentEntry)
            } else {
                hiltViewModel<EmotionViewModel>(entry)
            }
            EmotionMainRoute(
                navController = navController,
                viewModel = viewModel,
            )
        }

        composable(Routes.EMOTION_DETAILS) { entry ->
            val parentEntry: NavBackStackEntry? = remember(entry) {
                runCatching { navController.getBackStackEntry(Routes.EMOTION_GRAPH) }.getOrNull()
            }
            val viewModel: EmotionViewModel = if (parentEntry != null) {
                hiltViewModel<EmotionViewModel>(parentEntry)
            } else {
                hiltViewModel<EmotionViewModel>(entry)
            }
            EmotionDetailsRoute(
                navController = navController,
                viewModel = viewModel,
            )
        }

        composable(Routes.EMOTION_COMPLETE) {
            EmotionCompleteRoute(navController)
        }
    }
}