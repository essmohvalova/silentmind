package com.example.coursework_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.coursework_app.ui.emotion.EmotionScreen
import com.example.coursework_app.ui.journal.JournalScreen
import com.example.coursework_app.ui.main.MainScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        modifier = modifier
    ) {
        composable(route = Screen.Journal.route) {
            JournalScreen()
        }

        composable(route = Screen.Main.route) {
            MainScreen()
        }

        composable(route = Screen.Emotion.route) {
            EmotionScreen()
        }
    }
}