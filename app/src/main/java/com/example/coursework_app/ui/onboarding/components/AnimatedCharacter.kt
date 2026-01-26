package com.example.coursework_app.ui.onboarding.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.coursework_app.domain.model.CharacterType
import com.example.coursework_app.R

@Composable
fun AnimatedCharacter(
    characterType: CharacterType,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            when (characterType) {
                CharacterType.CAT -> R.raw.character_dog
                CharacterType.DOG -> R.raw.character_dog
                CharacterType.EMOJI -> R.raw.character_dog
            }
        )
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(200.dp),
            iterations = Int.MAX_VALUE // бесконечная анимация
        )
    }
}