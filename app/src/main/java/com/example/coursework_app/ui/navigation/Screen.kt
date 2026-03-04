package com.example.coursework_app.ui.navigation

import androidx.compose.ui.res.stringResource
import com.example.coursework_app.R

sealed class Screen(val route: String) {
    data object Main : Screen("main")
    data object Journal : Screen("journal")
    data object Emotion : Screen("emotion")
}

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int
) {
    data object Journal : BottomNavItem(
        route = Screen.Journal.route,
        title = "Журнал",
        icon = R.drawable.ic_arrow_right
    )

    data object Main : BottomNavItem(
        route = Screen.Main.route,
        title = "Главная",
        icon = R.drawable.ic_arrow_left
    )

    data object Emotion : BottomNavItem(
        route = Screen.Emotion.route,
        title = "Эмоции",
        icon = R.drawable.ic_back
    )

    companion object {
        val items = listOf(Journal, Main, Emotion)
    }
}