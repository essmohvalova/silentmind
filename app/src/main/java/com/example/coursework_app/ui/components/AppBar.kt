package com.example.coursework_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppBarTitle(
    title: String,
    modifier: Modifier = Modifier,
    size: AppBarSize = AppBarSize.MEDIUM,
    appearance: AppBarAppearance = AppBarAppearance.Default
) {
    val config = size.toConfig()

    val baseModifier = modifier
        .fillMaxWidth()
        .padding(horizontal = config.horizontalPadding)
        .padding(top = config.verticalPadding)
        .clip(RoundedCornerShape(config.cornerRadius))

    val styledModifier = when (appearance) {
        is AppBarAppearance.Default -> {
            baseModifier.background(MaterialTheme.colorScheme.surface)
        }
        is AppBarAppearance.Bordered -> {
            baseModifier
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = appearance.borderColor,
                    shape = RoundedCornerShape(config.cornerRadius)
                )
        }
        is AppBarAppearance.Gradient -> {
            baseModifier.background(
                brush = Brush.horizontalGradient(appearance.colors)
            )
        }
    }

    Box(
        modifier = styledModifier
            .padding(vertical = config.verticalPadding)
    ) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = config.fontSize,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
    }
}

enum class AppBarSize {
    SMALL,
    MEDIUM,
    LARGE
}

data class AppBarSizeConfig(
    val fontSize: TextUnit,
    val cornerRadius: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp
)

fun AppBarSize.toConfig(): AppBarSizeConfig {
    return when (this) {
        AppBarSize.SMALL -> AppBarSizeConfig(
            fontSize = 16.sp,
            cornerRadius = 16.dp,
            horizontalPadding = 12.dp,
            verticalPadding = 8.dp
        )
        AppBarSize.MEDIUM -> AppBarSizeConfig(
            fontSize = 20.sp,
            cornerRadius = 24.dp,
            horizontalPadding = 16.dp,
            verticalPadding = 12.dp
        )
        AppBarSize.LARGE -> AppBarSizeConfig(
            fontSize = 28.sp,
            cornerRadius = 32.dp,
            horizontalPadding = 20.dp,
            verticalPadding = 16.dp
        )
    }
}

sealed class AppBarAppearance {
    data object Default : AppBarAppearance()

    data class Bordered(
        val borderColor: Color
    ) : AppBarAppearance()

    data class Gradient(
        val colors: List<Color>
    ) : AppBarAppearance()
}
