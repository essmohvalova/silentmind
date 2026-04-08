package com.example.coursework_app.ui.navigation.welcome

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.coursework_app.ui.navigation.Routes
import com.example.coursework_app.ui.welcome.WelcomeScreen
import com.example.coursework_app.ui.welcome.WelcomeState
import com.example.coursework_app.ui.welcome.WelcomeViewModel

@Composable
fun WelcomeRoute(
    navController: NavHostController,
    viewModel: WelcomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WelcomeScreen(viewModel)

    LaunchedEffect(uiState.destination) {
        uiState.destination?.let {
            when (it) {
                WelcomeState.Destination.MAIN -> {
                    navController.navigate(Routes.BOTTOM) {
                        popUpTo(Routes.BOTTOM_MAIN) { inclusive = true }
                    }
                }
                WelcomeState.Destination.ONBOARDING -> {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            }
        }
    }
}