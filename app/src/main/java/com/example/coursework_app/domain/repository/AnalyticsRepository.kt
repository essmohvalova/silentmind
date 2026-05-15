package com.example.coursework_app.domain.repository

import com.example.coursework_app.domain.model.analytics.AnalyticsPeriod
import com.example.coursework_app.domain.model.analytics.AnalyticsSummary
import kotlinx.coroutines.flow.Flow

interface AnalyticsRepository {
    fun observeAnalytics(
        userId: String,
        period: AnalyticsPeriod,
        monthOffset: Int,
    ): Flow<AnalyticsSummary>
}
