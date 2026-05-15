package com.example.coursework_app.ui.navigation.bottom

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.coursework_app.ui.journal.JournalScreen
import com.example.coursework_app.ui.main.MainScreen
import com.example.coursework_app.ui.navigation.Routes
import com.example.coursework_app.ui.navigation.bottom.emotions.emotionNavGraph

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.BOTTOM_MAIN,
        modifier = modifier
    ) {

        composable(Routes.BOTTOM_MAIN) {
            MainScreen(navController = navController)
        }

        composable(Routes.BOTTOM_JOURNAL) {
            JournalScreen()
        }

        emotionNavGraph(navController)
    }
}
