package com.example.coursework_app // ИЛИ ваш реальный package name


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coursework_app.ui.onboarding.navigation.OnboardingDestination
import com.example.coursework_app.ui.onboarding.screens.CharacterSelectionScreen
import com.example.coursework_app.ui.onboarding.screens.RegistrationScreen
import com.example.coursework_app.ui.onboarding.screens.WelcomeScreen
import com.example.coursework_app.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.coursework_app.ui.theme.EmotionTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EmotionTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    EmotionTrackerApp()
                }
            }
        }
    }
}

@Composable
fun EmotionTrackerApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = OnboardingDestination.Welcome.route
    ) {
        // 1. Welcome Screen
        composable(OnboardingDestination.Welcome.route) {
            WelcomeScreen(
                onNext = { navController.navigate(OnboardingDestination.CharacterSelection.route) }
            )
        }

        // 2. Character Selection Screen
        composable(OnboardingDestination.CharacterSelection.route) {
            val viewModel: OnboardingViewModel = viewModel()
            val selectedCharacter by viewModel.selectedCharacter.collectAsState()

            CharacterSelectionScreen(
                selectedCharacter = selectedCharacter,
                onCharacterSelected = viewModel::selectCharacter,
                onNext = { navController.navigate(OnboardingDestination.Registration.route) },
                onBack = { navController.popBackStack() }
            )
        }

        // 3. Registration Screen
        composable(OnboardingDestination.Registration.route) {
            val viewModel: OnboardingViewModel = viewModel()
            val userName by viewModel.userName.collectAsState()

            RegistrationScreen(
                userName = userName,
                onNameChanged = viewModel::updateUserName,
                onComplete = {
                    viewModel.saveUserData()
                    navController.navigate("main")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // 4. Главный экран
        composable("main") {
            MainScreen()
        }
    }
}


@Composable
fun MainScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "🎉 Онбординг завершен!",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Главный экран в разработке...")
        }
    }
}