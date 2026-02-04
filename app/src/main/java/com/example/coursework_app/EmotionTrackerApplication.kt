package com.example.coursework_app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class EmotionTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Базовый код без инициализации
    }
}