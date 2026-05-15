package com.example.coursework_app.ui.navigation.routes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.coursework_app.ui.navigation.Routes
import com.example.coursework_app.ui.onboarding.OnboardingScreen
import com.example.coursework_app.ui.onboarding.OnboardingViewModel

@Composable
fun OnboardingRoute(
    navController: NavHostController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.onboardingCompleted) {
        if (state.onboardingCompleted) {
            navController.navigate(Routes.BOTTOM) {
                popUpTo(Routes.ONBOARDING) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    OnboardingScreen(viewModel = viewModel)
}
