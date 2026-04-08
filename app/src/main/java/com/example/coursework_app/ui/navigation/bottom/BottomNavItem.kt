package com.example.coursework_app.ui.navigation.bottom

import com.example.coursework_app.R
import com.example.coursework_app.ui.navigation.Routes

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int
) {

    object Main : BottomNavItem(
        route = Routes.BOTTOM_MAIN,
        title = "Главная",
        icon = R.drawable.ic_arrow_left
    )

    object Journal : BottomNavItem(
        route = Routes.BOTTOM_JOURNAL,
        title = "Журнал",
        icon = R.drawable.ic_arrow_right
    )

    object Emotion : BottomNavItem(
        route = Routes.EMOTION_GRAPH, // 👈 ключевой момент
        title = "Эмоции",
        icon = R.drawable.ic_back
    )

    companion object {
        val items = listOf(Main, Journal, Emotion)
    }
}