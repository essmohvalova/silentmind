package com.example.coursework_app.ui.navigation.bottom.emotions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import android.util.Log
import androidx.navigation.NavHostController
import com.example.coursework_app.ui.emotion.EmotionDetailsScreen
import com.example.coursework_app.ui.emotion.EmotionViewModel
import com.example.coursework_app.ui.navigation.Routes

@Composable
fun EmotionDetailsRoute(
    navController: NavHostController,
    viewModel: EmotionViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                EmotionViewModel.Event.Saved -> {
                    navController.navigate(Routes.BOTTOM_MAIN) {
                        popUpTo(Routes.EMOTION_GRAPH) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
                is EmotionViewModel.Event.SaveFailed -> {
                    Log.e("EmotionDetailsRoute", "Save failed: ${event.reason}")
                }
            }
        }
    }

    EmotionDetailsScreen(
        viewModel = viewModel,
        onBackClick = navController::popBackStack,
        onSaveClick = viewModel::save,
    )
}