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
        icon = R.drawable.ic_menu_home
    )

    object Journal : BottomNavItem(
        route = Routes.BOTTOM_JOURNAL,
        title = "Журнал",
        icon = R.drawable.ic_navbar_journal
    )

    object Emotion : BottomNavItem(
        route = Routes.EMOTION_GRAPH,
        title = "Эмоции",
        icon = R.drawable.ic_navbar_mood
    )

    object Practice : BottomNavItem(
        route = Routes.PRACTICE_GRAPH,
        title = "Практики",
        icon = R.drawable.ic_navbar_practice
    )

    companion object {
        val items = listOf(Main, Journal, Emotion, Practice)
    }
}