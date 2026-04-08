package com.example.coursework_app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.coursework_app.ui.navigation.bottom.BottomNavScreen
import com.example.coursework_app.ui.navigation.routes.OnboardingRoute
import com.example.coursework_app.ui.navigation.welcome.WelcomeRoute

@Composable
fun RootNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME
    ) {
        composable(Routes.WELCOME) {
            WelcomeRoute(navController)
        }

        composable(Routes.ONBOARDING) {
            OnboardingRoute(navController)
        }

        composable(Routes.BOTTOM) {
            BottomNavScreen()
        }
    }
}