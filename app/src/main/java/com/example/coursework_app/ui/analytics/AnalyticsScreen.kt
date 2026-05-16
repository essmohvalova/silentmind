package com.example.coursework_app.ui.analytics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.coursework_app.domain.model.analytics.AnalyticsPeriod
import com.example.coursework_app.domain.model.analytics.AnalyticsSummary
import com.example.coursework_app.domain.model.analytics.CalendarDayEmotion
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel,
    onBackClick: () -> Unit,
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            AnalyticsHeader(
                selectedPeriod = state.period,
                onSelectPeriod = viewModel::onSelectPeriod,
                onBackClick = onBackClick,
            )
        }
        if (state.isEmpty) {
            item {
                EmptyAnalyticsState()
            }
            return@LazyColumn
        }
        state.summary?.let { summary ->
            item { InsightCard(summary.insightText) }
            item { MoodTrendChart(summary = summary) }
            item { EmotionDistributionCard(summary = summary) }
            item {
                MoodCalendarCard(
                    entries = summary.moodCalendar,
                    monthOffset = state.monthOffset,
                    onPreviousMonth = viewModel::onPreviousMonth,
                    onNextMonth = viewModel::onNextMonth,
                )
            }
            item { TriggerInsightsCard(summary = summary) }
            item { PracticeImpactCard(summary = summary) }
        }
        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun AnalyticsHeader(
    selectedPeriod: AnalyticsPeriod,
    onSelectPeriod: (AnalyticsPeriod) -> Unit,
    onBackClick: () -> Unit,
) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF6F2FF))) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(Modifier.weight(1f)) {
                    Text("Аналитика настроения", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                    Text("Лучше понимая себя, легче заботиться о себе", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PeriodChip("7 дней", selectedPeriod == AnalyticsPeriod.WEEK) { onSelectPeriod(AnalyticsPeriod.WEEK) }
                PeriodChip("30 дней", selectedPeriod == AnalyticsPeriod.MONTH) { onSelectPeriod(AnalyticsPeriod.MONTH) }
                PeriodChip("Все время", selectedPeriod == AnalyticsPeriod.ALL) { onSelectPeriod(AnalyticsPeriod.ALL) }
            }
        }
    }
}

@Composable
private fun PeriodChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(99.dp),
        color = if (selected) Color(0xFFDCCFF7) else Color.White,
    ) { Text(text, modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)) }
}

@Composable
private fun InsightCard(text: String) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF7F2))) {
        Column(Modifier.padding(16.dp)) {
            Text("Вывод периода", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(6.dp))
            Text(text, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun MoodTrendChart(summary: AnalyticsSummary) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F7FF))) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text("Динамика настроения", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))
            if (summary.trendPoints.isEmpty()) {
                Text("Недостаточно данных для графика")
                return@Column
            }
            Canvas(modifier = Modifier.fillMaxWidth().height(170.dp)) {
                val width = size.width
                val height = size.height
                val points = summary.trendPoints
                val maxY = 5f
                val minY = 1f
                val stepX = if (points.size > 1) width / (points.size - 1) else width
                val path = Path()
                points.forEachIndexed { index, point ->
                    val x = stepX * index
                    val normalized = (point.avgIntensity - minY) / (maxY - minY)
                    val y = height - normalized * (height - 24f)
                    if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
                }
                drawPath(
                    path = path,
                    brush = Brush.horizontalGradient(listOf(Color(0xFF8FD4C1), Color(0xFF9CB8F5))),
                    style = Stroke(width = 8f, cap = StrokeCap.Round),
                )
                points.forEachIndexed { index, point ->
                    val x = stepX * index
                    val normalized = (point.avgIntensity - minY) / (maxY - minY)
                    val y = height - normalized * (height - 24f)
                    drawCircle(color = emotionColor(point.dominantEmotion), radius = 8f, center = Offset(x, y))
                }
            }
        }
    }
}

@Composable
private fun EmotionDistributionCard(summary: AnalyticsSummary) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8EF))) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Распределение эмоций", style = MaterialTheme.typography.titleMedium)
            summary.emotionDistribution.forEach { item ->
                Column {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${item.emotion} - ${item.count}")
                        Text("${item.percentage.toInt()}%")
                    }
                    LinearProgressIndicator(
                        progress = { item.percentage / 100f },
                        modifier = Modifier.fillMaxWidth().height(10.dp),
                        color = emotionColor(item.emotion),
                        trackColor = Color.White,
                    )
                }
            }
        }
    }
}

@Composable
private fun MoodCalendarCard(
    entries: List<CalendarDayEmotion>,
    monthOffset: Int,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
) {
    val monthTitle = rememberMonthTitle(monthOffset)
    val byDay = entries.associateBy { dayOfMonth(it.timestamp) }
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF3FBFF))) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Календарь настроения", style = MaterialTheme.typography.titleMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onPreviousMonth) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null) }
                    Text(monthTitle)
                    IconButton(onClick = onNextMonth) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null) }
                }
            }
            Spacer(Modifier.height(8.dp))
            val daysInMonth = currentMonthDays(monthOffset)
            val rows = (daysInMonth + 6) / 7
            repeat(rows) { row ->
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(7) { col ->
                        val day = row * 7 + col + 1
                        if (day <= daysInMonth) {
                            val emotion = byDay[day]?.emotion
                            Box(
                                modifier = Modifier.weight(1f).height(34.dp).background(
                                    color = emotion?.let(::emotionColor)?.copy(alpha = 0.45f) ?: Color(0xFFEAEAEA),
                                    shape = RoundedCornerShape(10.dp),
                                ),
                                contentAlignment = Alignment.Center,
                            ) { Text(day.toString(), style = MaterialTheme.typography.labelMedium) }
                        } else {
                            Spacer(modifier = Modifier.weight(1f).height(34.dp))
                        }
                    }
                }
                Spacer(Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun TriggerInsightsCard(summary: AnalyticsSummary) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F4FF))) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Триггеры и эмоции", style = MaterialTheme.typography.titleMedium)
            if (summary.triggerAssociations.isEmpty()) {
                Text("Недостаточно тегов для анализа триггеров")
                return@Column
            }
            summary.triggerAssociations.take(5).forEach {
                Text("${it.tag}: ${it.dominantEmotion} (${it.percentage.toInt()}%)")
            }
        }
    }
}

@Composable
private fun PracticeImpactCard(summary: AnalyticsSummary) {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF8FF))) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Влияние практик", style = MaterialTheme.typography.titleMedium)
            if (summary.practiceImpact.isEmpty()) {
                Text("Недостаточно данных для анализа")
                return@Column
            }
            summary.practiceImpact.forEach { impact ->
                val sign = if (impact.positiveEmotionDelta >= 0) "+" else ""
                Text("${impact.practiceTag}: эмоции $sign${"%.2f".format(impact.positiveEmotionDelta)}, сессий ${impact.comparedSessions}")
            }
        }
    }
}

@Composable
private fun EmptyAnalyticsState() {
    Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F8F8))) {
        Column(
            Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Box(Modifier.size(56.dp).background(Color(0xFFE3E3E3), CircleShape))
            Text("Пока нет данных для аналитики", style = MaterialTheme.typography.titleMedium)
            Text("Добавь первую запись настроения", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

private fun emotionColor(emotion: String): Color = when (emotion.lowercase()) {
    "радость" -> Color(0xFFFFD081)
    "спокойствие" -> Color(0xFF9FDCC8)
    "грусть" -> Color(0xFFAEC7F4)
    "тревога" -> Color(0xFFD1BDF0)
    "злость" -> Color(0xFFF1B2A7)
    "усталость" -> Color(0xFFBFB7D9)
    else -> Color(0xFFD8D8D8)
}

private fun dayOfMonth(timestamp: Long): Int {
    return Calendar.getInstance().apply { timeInMillis = timestamp }.get(Calendar.DAY_OF_MONTH)
}

private fun currentMonthDays(offset: Int): Int {
    val c = Calendar.getInstance()
    c.add(Calendar.MONTH, offset)
    return c.getActualMaximum(Calendar.DAY_OF_MONTH)
}

private fun rememberMonthTitle(offset: Int): String {
    val c = Calendar.getInstance()
    c.add(Calendar.MONTH, offset)
    return SimpleDateFormat("LLLL yyyy", Locale.forLanguageTag("ru")).format(Date(c.timeInMillis))
}
