package com.example.coursework_app.ui.onboarding.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.coursework_app.ui.onboarding.screens.CharacterSelectionScreen
import com.example.coursework_app.ui.onboarding.screens.RegistrationScreen
import com.example.coursework_app.ui.onboarding.screens.WelcomeScreen
import com.example.coursework_app.ui.onboarding.viewmodel.OnboardingViewModel

@Composable
fun OnboardingNavHost(
    navController: NavHostController,
    viewModel: OnboardingViewModel,
    onComplete: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = OnboardingDestination.Welcome.route
    ) {
        composable(OnboardingDestination.Welcome.route) {
            WelcomeScreen(
                onNext = { navController.navigate(OnboardingDestination.CharacterSelection.route) }
            )
        }

        composable(OnboardingDestination.CharacterSelection.route) {
            val selectedCharacter by viewModel.selectedCharacter.collectAsState()

            CharacterSelectionScreen(
                selectedCharacter = selectedCharacter,
                onCharacterSelected = viewModel::selectCharacter,
                onNext = { navController.navigate(OnboardingDestination.Registration.route) },
                onBack = { navController.popBackStack() }
            )
        }

        composable(OnboardingDestination.Registration.route) {
            val userName by viewModel.userName.collectAsState()

            RegistrationScreen(
                userName = userName,
                onNameChanged = viewModel::updateUserName,
                onComplete = {
                    viewModel.saveUserData()
                    onComplete()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}