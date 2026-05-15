package com.example.coursework_app.ui.navigation.bottom.emotions

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.coursework_app.ui.analytics.AnalyticsScreen
import com.example.coursework_app.ui.analytics.AnalyticsViewModel
import com.example.coursework_app.ui.navigation.Routes

@Composable
fun EmotionAnalyticsRoute(
    navController: NavHostController,
    viewModel: AnalyticsViewModel = hiltViewModel(),
) {
    AnalyticsScreen(
        viewModel = viewModel,
        onBackClick = {
            navController.navigate(Routes.EMOTION_MAIN)
        },
    )
}
