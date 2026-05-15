package com.example.coursework_app.ui.navigation.bottom.emotions

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.coursework_app.ui.emotion.EmotionMainScreen
import com.example.coursework_app.ui.emotion.EmotionViewModel
import com.example.coursework_app.ui.navigation.Routes

@Composable
fun EmotionMainRoute(
    navController: NavHostController,
    viewModel: EmotionViewModel,
) {
    EmotionMainScreen(
        viewModel = viewModel,
        onNextClick = {
            navController.navigate(Routes.EMOTION_DETAILS)
        }
    )
}