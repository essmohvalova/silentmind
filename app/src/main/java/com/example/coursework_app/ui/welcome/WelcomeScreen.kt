package com.example.coursework_app.ui.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.coursework_app.R

@Composable
fun WelcomeScreen(
    viewModel: WelcomeViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.character_dog)
    )

    AnimatedVisibility(
        visible = uiState.isLoading && composition != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Загружаемся...", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(24.dp))

            LottieAnimation(
                composition = composition!!,
                modifier = Modifier.size(200.dp),
                iterations = Int.MAX_VALUE
            )
        }
    }
}