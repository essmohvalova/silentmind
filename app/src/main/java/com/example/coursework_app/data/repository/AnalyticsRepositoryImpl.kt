package com.example.coursework_app.data.repository

import com.example.coursework_app.domain.model.analytics.AnalyticsPeriod
import com.example.coursework_app.domain.model.analytics.AnalyticsSummary
import com.example.coursework_app.domain.model.analytics.CalendarDayEmotion
import com.example.coursework_app.domain.model.analytics.EmotionDistributionItem
import com.example.coursework_app.domain.model.analytics.PracticeImpact
import com.example.coursework_app.domain.model.analytics.TrendPoint
import com.example.coursework_app.domain.model.analytics.TriggerAssociation
import com.example.coursework_app.domain.model.mood.MoodEntry
import com.example.coursework_app.domain.repository.AnalyticsRepository
import com.example.coursework_app.domain.repository.MoodEntryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    private val moodEntryRepository: MoodEntryRepository,
) : AnalyticsRepository {

    override fun observeAnalytics(
        userId: String,
        period: AnalyticsPeriod,
        monthOffset: Int,
    ): Flow<AnalyticsSummary> {
        return moodEntryRepository.observeMoodEntries(userId).map { allEntries ->
            val periodEntries = filterByPeriod(allEntries, period)
            AnalyticsSummary(
                insightText = buildInsightText(allEntries, periodEntries, period),
                trendPoints = buildTrendPoints(periodEntries),
                emotionDistribution = buildEmotionDistribution(periodEntries),
                moodCalendar = buildMoodCalendar(allEntries, monthOffset),
                triggerAssociations = buildTriggerAssociations(periodEntries),
                practiceImpact = buildPracticeImpact(periodEntries),
                filteredEntries = periodEntries,
            )
        }
    }

    private fun filterByPeriod(entries: List<MoodEntry>, period: AnalyticsPeriod): List<MoodEntry> {
        val days = period.days ?: return entries.sortedBy { it.createdAt }
        val threshold = System.currentTimeMillis() - days * DAY_MS
        return entries.filter { it.createdAt >= threshold }.sortedBy { it.createdAt }
    }

    private fun buildTrendPoints(entries: List<MoodEntry>): List<TrendPoint> {
        val grouped = entries.groupBy { toDayKey(it.createdAt) }
        return grouped.entries.sortedBy { it.key.epochDay }.map { dayGroup ->
            val dayEntries = dayGroup.value
            val avgIntensity = dayEntries.map { it.intensity }.average().toFloat()
            val balanceScore = dayEntries.map { emotionScore(it.emotion) }.average().toFloat()
            val dominantEmotion = dayEntries
                .groupingBy { it.emotion }
                .eachCount()
                .maxByOrNull { it.value }
                ?.key
                .orEmpty()
            TrendPoint(
                timestamp = dayGroup.key.startMillis,
                avgIntensity = avgIntensity,
                balanceScore = balanceScore,
                dominantEmotion = dominantEmotion,
                entriesCount = dayEntries.size,
            )
        }
    }

    private fun buildEmotionDistribution(entries: List<MoodEntry>): List<EmotionDistributionItem> {
        if (entries.isEmpty()) return emptyList()
        val total = entries.size.toFloat()
        return entries
            .groupingBy { it.emotion }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .map { (emotion, count) ->
                EmotionDistributionItem(
                    emotion = emotion,
                    count = count,
                    percentage = (count / total) * 100f,
                )
            }
    }

    private fun buildMoodCalendar(entries: List<MoodEntry>, monthOffset: Int): List<CalendarDayEmotion> {
        val target = Calendar.getInstance().apply {
            add(Calendar.MONTH, monthOffset)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val month = target.get(Calendar.MONTH)
        val year = target.get(Calendar.YEAR)
        val monthEntries = entries.filter { entry ->
            val c = Calendar.getInstance().apply { timeInMillis = entry.createdAt }
            c.get(Calendar.MONTH) == month && c.get(Calendar.YEAR) == year
        }
        return monthEntries.groupBy { toDayKey(it.createdAt) }
            .entries
            .sortedBy { it.key.epochDay }
            .map { (dayKey, dayEntries) ->
                val topEmotion = dayEntries.groupingBy { it.emotion }
                    .eachCount()
                    .maxByOrNull { it.value }
                    ?.key
                    .orEmpty()
                CalendarDayEmotion(
                    timestamp = dayKey.startMillis,
                    emotion = topEmotion,
                    entriesCount = dayEntries.size,
                )
            }
    }

    private fun buildTriggerAssociations(entries: List<MoodEntry>): List<TriggerAssociation> {

        /*val tagged = entries.filter { it.tags.isNotEmpty() }
        val byTag = mutableMapOf<String, MutableList<MoodEntry>>()
        tagged.forEach { entry ->
            entry.tags.forEach { rawTag ->
                val tag = rawTag.trim()
                if (tag.isNotEmpty()) {
                    byTag.getOrPut(tag) { mutableListOf() }.add(entry)
                }
            }
        }
        return byTag.entries
            .mapNotNull { (tag, tagEntries) ->
                val counts = tagEntries.groupingBy { it.emotion }.eachCount()
                val dominant = counts.maxByOrNull { it.value } ?: return@mapNotNull null
                TriggerAssociation(
                    tag = tag,
                    dominantEmotion = dominant.key,
                    percentage = dominant.value * 100f / tagEntries.size,
                    totalTaggedEntries = tagEntries.size,
                )
            }
            .sortedByDescending { it.totalTaggedEntries }*/

        // Tags are not persisted in MoodEntry yet; return empty until the model supports them.
        return emptyList()
    }

    private fun buildPracticeImpact(entries: List<MoodEntry>): List<PracticeImpact> {

        /*val ordered = entries.sortedBy { it.createdAt }
        val practiceCandidates = ordered.filter { entry ->
            entry.tags.any { tag -> isPracticeTag(tag) }
        }
        val impacts = mutableMapOf<String, MutableList<Pair<Float, Float>>>()
        practiceCandidates.forEach { current ->
            val previous = ordered.lastOrNull { it.createdAt < current.createdAt && current.createdAt - it.createdAt <= DAY_MS }
            val next = ordered.firstOrNull { it.createdAt > current.createdAt && it.createdAt - current.createdAt <= DAY_MS }
            if (previous != null && next != null) {
                val intensityDelta = next.intensity - previous.intensity.toFloat()
                val moodDelta = emotionScore(next.emotion) - emotionScore(previous.emotion)
                current.tags.filter(::isPracticeTag).forEach { tag ->
                    impacts.getOrPut(tag) { mutableListOf() }.add(intensityDelta to moodDelta)
                }
            }
        }
        return impacts.entries.mapNotNull { (tag, deltas) ->
            if (deltas.size < 2) return@mapNotNull null
            PracticeImpact(
                practiceTag = tag,
                comparedSessions = deltas.size,
                avgIntensityDelta = deltas.map { it.first }.average().toFloat(),
                positiveEmotionDelta = deltas.map { it.second }.average().toFloat(),
            )
        }.sortedByDescending { it.comparedSessions }*/

        // Tags are not persisted in MoodEntry yet; return empty until the model supports them.
        return emptyList()
    }

    private fun buildInsightText(
        allEntries: List<MoodEntry>,
        periodEntries: List<MoodEntry>,
        period: AnalyticsPeriod,
    ): String {
        if (periodEntries.isEmpty()) return "Пока недостаточно данных. Добавьте записи, чтобы увидеть личные закономерности."
        val dominant = periodEntries.groupingBy { it.emotion }.eachCount().maxByOrNull { it.value }?.key.orEmpty()
        val anxietyShareCurrent = percentageForEmotion(periodEntries, "Тревога")
        val previous = previousPeriodEntries(allEntries, period)
        if (previous.isNotEmpty()) {
            val anxietySharePrevious = percentageForEmotion(previous, "Тревога")
            val anxietyDiff = anxietyShareCurrent - anxietySharePrevious
            if (anxietyDiff <= -8f) return "Уровень тревоги снизился по сравнению с предыдущим периодом."
            if (anxietyDiff >= 8f) return "За текущий период тревога встречается чаще, чем раньше."
        }
        return when (dominant) {
            "Спокойствие" -> "За выбранный период чаще всего вы отмечали спокойствие."
            "Радость" -> "Радость стала ведущим состоянием в выбранном периоде."
            else -> "Сейчас чаще всего встречается эмоция \"$dominant\"."
        }
    }

    private fun previousPeriodEntries(
        allEntries: List<MoodEntry>,
        period: AnalyticsPeriod,
    ): List<MoodEntry> {
        val days = period.days ?: return emptyList()
        val now = System.currentTimeMillis()
        val currentFrom = now - days * DAY_MS
        val previousFrom = currentFrom - days * DAY_MS
        return allEntries.filter { it.createdAt in previousFrom until currentFrom }
    }

    private fun percentageForEmotion(entries: List<MoodEntry>, emotion: String): Float {
        if (entries.isEmpty()) return 0f
        return entries.count { it.emotion == emotion } * 100f / entries.size
    }

    private fun emotionScore(emotion: String): Float = when (emotion.lowercase()) {
        "радость" -> 1f
        "спокойствие" -> 0.8f
        "грусть" -> -0.6f
        "тревога" -> -1f
        "злость" -> -0.8f
        "усталость" -> -0.4f
        else -> 0f
    }

    private fun isPracticeTag(tag: String): Boolean {
        val value = tag.lowercase()
        return value.contains("практи") || value.contains("дыхан") || value.contains("медита")
    }

    private fun toDayKey(timestamp: Long): DayKey {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val epochDay = calendar.timeInMillis / DAY_MS
        return DayKey(startMillis = calendar.timeInMillis, epochDay = epochDay)
    }

    private data class DayKey(
        val startMillis: Long,
        val epochDay: Long,
    )

    private companion object {
        const val DAY_MS = 24L * 60L * 60L * 1000L
    }
}
