package com.example.coursework_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.coursework_app.ui.navigation.BottomNavScreen
import com.example.coursework_app.ui.onboarding.OnboardingScreen
import com.example.coursework_app.ui.theme.CourseworkAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CourseworkAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var showOnboarding by remember { mutableStateOf(true) }

                    if (showOnboarding) {
                        OnboardingScreen(
                            onOnboardingComplete = {
                                showOnboarding = false
                            }
                        )
                    } else {
                        BottomNavScreen()
                    }
                }
            }
        }
    }
}