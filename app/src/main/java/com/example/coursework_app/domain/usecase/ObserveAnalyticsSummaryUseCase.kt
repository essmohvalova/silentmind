package com.example.coursework_app.domain.usecase

import com.example.coursework_app.domain.model.analytics.AnalyticsPeriod
import com.example.coursework_app.domain.model.analytics.AnalyticsSummary
import com.example.coursework_app.domain.repository.AnalyticsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ObserveAnalyticsSummaryUseCase {

    operator fun invoke(
        userId: String,
        period: AnalyticsPeriod,
        monthOffset: Int,
    ): Flow<AnalyticsSummary>
}

class ObserveAnalyticsSummaryUseCaseImpl @Inject constructor(
    private val analyticsRepository: AnalyticsRepository,
) : ObserveAnalyticsSummaryUseCase {

    override fun invoke(
        userId: String,
        period: AnalyticsPeriod,
        monthOffset: Int,
    ): Flow<AnalyticsSummary> =
        analyticsRepository.observeAnalytics(
            userId = userId,
            period = period,
            monthOffset = monthOffset,
        )
}
