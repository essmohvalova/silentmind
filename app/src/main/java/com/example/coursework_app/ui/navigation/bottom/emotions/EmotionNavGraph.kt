package com.example.coursework_app.ui.navigation.bottom.emotions

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.coursework_app.ui.emotion.EmotionScreen
import com.example.coursework_app.ui.navigation.Routes

fun NavGraphBuilder.emotionNavGraph(
    navController: NavHostController
) {
    navigation(
        route = Routes.EMOTION_GRAPH,
        startDestination = Routes.EMOTION_MAIN
    ) {
        composable(Routes.EMOTION_MAIN) {
            EmotionScreen()
        }
    }
}