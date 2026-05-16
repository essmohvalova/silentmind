package com.example.coursework_app.domain.model.analytics

import com.example.coursework_app.domain.model.mood.MoodEntry

enum class AnalyticsPeriod(val days: Int?) {
    WEEK(7),
    MONTH(30),
    ALL(null),
}

data class TrendPoint(
    val timestamp: Long,
    val avgIntensity: Float,
    val balanceScore: Float,
    val dominantEmotion: String,
    val entriesCount: Int,
)

data class EmotionDistributionItem(
    val emotion: String,
    val count: Int,
    val percentage: Float,
)

data class CalendarDayEmotion(
    val timestamp: Long,
    val emotion: String,
    val entriesCount: Int,
)

data class TriggerAssociation(
    val tag: String,
    val dominantEmotion: String,
    val percentage: Float,
    val totalTaggedEntries: Int,
)

data class PracticeImpact(
    val practiceTag: String,
    val comparedSessions: Int,
    val avgIntensityDelta: Float,
    val positiveEmotionDelta: Float,
)

data class AnalyticsSummary(
    val insightText: String,
    val trendPoints: List<TrendPoint>,
    val emotionDistribution: List<EmotionDistributionItem>,
    val moodCalendar: List<CalendarDayEmotion>,
    val triggerAssociations: List<TriggerAssociation>,
    val practiceImpact: List<PracticeImpact>,
    val filteredEntries: List<MoodEntry>,
)
