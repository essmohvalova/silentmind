package com.example.coursework_app.ui.navigation.bottom.emotions

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.coursework_app.ui.emotion.EmotionCompleteScreen
import com.example.coursework_app.ui.navigation.Routes

@Composable
fun EmotionCompleteRoute(
    navController: NavHostController,
) {
    EmotionCompleteScreen(
        onDoneClick = {
            navController.navigate(Routes.BOTTOM_MAIN) {
                popUpTo(Routes.EMOTION_COMPLETE) {
                    inclusive = true
                }
            }
        }
    )
}