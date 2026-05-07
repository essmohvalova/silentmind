package com.example.coursework_app.ui.journal

import android.app.DatePickerDialog
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.coursework_app.domain.model.emotion.Emotion
import com.example.coursework_app.domain.model.mood.MoodEntry
import com.example.coursework_app.ui.navigation.Routes
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun JournalScreen(
    navController: NavHostController,
    viewModel: JournalViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current
    var showSortMenu by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Журнал",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        JournalEmotionFilterRow(
            selectedEmotion = uiState.selectedEmotion,
            onSelect = viewModel::selectEmotionFilter,
        )

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            JournalActionButton(
                label = "📅",
                contentDescription = "Выбрать дату",
                onClick = {
                    val now = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, day ->
                            val calendar = Calendar.getInstance()
                            calendar.set(year, month, day, 0, 0, 0)
                            calendar.set(Calendar.MILLISECOND, 0)
                            viewModel.selectDateFromMillis(calendar.timeInMillis)
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH),
                    ).show()
                },
            )
            Spacer(modifier = Modifier.weight(1f))
            Box {
                JournalActionButton(
                    label = "⇅",
                    contentDescription = "Сортировка",
                    onClick = { showSortMenu = true },
                )
                JournalSortSheet(
                    expanded = showSortMenu,
                    currentSort = uiState.sortMode,
                    onDismiss = { showSortMenu = false },
                    onSelect = {
                        viewModel.setSortMode(it)
                        showSortMenu = false
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        JournalCalendarStrip(
            weekStartMillis = uiState.weekStartMillis,
            entries = uiState.entries,
            selectedDate = uiState.selectedDate,
            onPreviousWeek = { viewModel.shiftWeek(-1) },
            onNextWeek = { viewModel.shiftWeek(1) },
            onSelectDate = viewModel::selectDate,
            onShowAllPeriod = viewModel::showAllPeriod,
        )

        Spacer(modifier = Modifier.height(16.dp))
        JournalScreenContent(
            uiState = uiState,
            onCardClick = {
                // TODO: Navigate to journal entry details when route is ready.
            },
            onAddEntryClick = { navController.navigate(Routes.EMOTION_GRAPH) },
            onResetFilters = viewModel::clearFilters,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun JournalScreenContent(
    uiState: JournalUiState,
    onCardClick: (MoodEntry) -> Unit,
    onAddEntryClick: () -> Unit,
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (uiState.entries.isEmpty()) {
        JournalEmptyState(
            title = "Пока нет записей",
            subtitle = "Отметь эмоцию, чтобы она появилась в журнале",
            actionText = "Добавить запись",
            onAction = onAddEntryClick,
            modifier = modifier,
        )
        return
    }

    if (uiState.filteredEntries.isEmpty()) {
        JournalEmptyState(
            title = "Нет записей по выбранным фильтрам",
            subtitle = "Попробуй изменить фильтры или выбрать другую дату",
            actionText = "Сбросить фильтры",
            onAction = onResetFilters,
            modifier = modifier,
        )
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Text(
                text = "Записей: ${uiState.entriesCount}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        items(uiState.filteredEntries, key = { it.id }) { entry ->
            JournalEntryCard(
                entry = entry,
                onClick = { onCardClick(entry) },
            )
        }
    }
}

@Composable
private fun JournalActionButton(
    label: String,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        onClick = onClick,
    ) {
        Box(modifier = Modifier.size(40.dp), contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun JournalEmotionFilterRow(
    selectedEmotion: String?,
    onSelect: (String?) -> Unit,
) {
    val filters = listOf("Все записи", "Радость", "Спокойствие", "Грусть", "Тревога", "Злость", "Усталость")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(filters) { filter ->
            val emotion = if (filter == "Все записи") null else filter
            val selected = selectedEmotion == emotion || (emotion == null && selectedEmotion == null)
            val accent = emotionAccentColor(emotion ?: "Все записи")
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = if (selected) accent else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current,
                ) { onSelect(emotion) },
            ) {
                Text(
                    text = filter,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium),
                )
            }
        }
    }
}

@Composable
private fun JournalCalendarStrip(
    weekStartMillis: Long,
    entries: List<MoodEntry>,
    selectedDate: JournalDateKey?,
    onPreviousWeek: () -> Unit,
    onNextWeek: () -> Unit,
    onSelectDate: (JournalDateKey) -> Unit,
    onShowAllPeriod: () -> Unit,
) {
    var expanded by remember { mutableStateOf(true) }
    val weekDays = remember(weekStartMillis) { buildWeekDays(weekStartMillis) }
    val monthTitle = remember(weekStartMillis) {
        SimpleDateFormat("LLLL yyyy", Locale("ru")).format(Date(weekStartMillis))
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("ru")) else it.toString() }
    }
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)),
        modifier = Modifier.animateContentSize(),
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = if (selectedDate == null) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = LocalIndication.current,
                    ) {
                        if (selectedDate == null) {
                            onSelectDate(todayDateKey())
                        } else {
                            onShowAllPeriod()
                        }
                    },
                ) {
                    Text(
                        text = "За весь период",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selectedDate == null) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = onPreviousWeek, modifier = Modifier.size(26.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Прошлая неделя")
                }
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = monthTitle, style = MaterialTheme.typography.titleMedium)
                }
                IconButton(onClick = onNextWeek, modifier = Modifier.size(26.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Следующая неделя")
                }
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(999.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier
                        .size(36.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = LocalIndication.current,
                        ) { expanded = !expanded },
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (expanded) "˄" else "˅",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            if (expanded) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(86.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(weekDays) { day ->
                        val dateKey = JournalDateKey(day.year, day.month, day.day)
                        val selected = selectedDate == dateKey
                        val dominantEmotion = dominantEmotion(entries, dateKey)
                        val emotionBg = dominantEmotion?.let { emotionAccentColor(it).copy(alpha = 0.24f) }
                            ?: MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.22f)
                        val animatedBg by animateColorAsState(
                            targetValue = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)
                            else emotionBg,
                            animationSpec = tween(220),
                            label = "calendarDayBg",
                        )
                        val animatedWidth by animateDpAsState(
                            targetValue = if (selected) 56.dp else 50.dp,
                            animationSpec = tween(220),
                            label = "calendarDayWidth",
                        )
                        val dayScale by animateFloatAsState(
                            targetValue = if (selected) 1f else 0.98f,
                            animationSpec = tween(220),
                            label = "calendarDayScale",
                        )
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = animatedBg,
                            modifier = Modifier
                                .width(animatedWidth)
                                .graphicsLayer {
                                    scaleX = dayScale
                                    scaleY = dayScale
                                }
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = LocalIndication.current,
                                ) { onSelectDate(dateKey) },
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 5.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Text(day.weekday, style = MaterialTheme.typography.labelSmall)
                                Text(
                                    day.day.toString(),
                                    style = if (selected) MaterialTheme.typography.titleSmall else MaterialTheme.typography.labelLarge,
                                )
                            }
                        }
                    }
                }
            } else {
                val compactText = selectedDate?.let {
                    "${it.day} ${monthNameForIndex(it.month)} • ${weekdayShortByDate(it)}"
                } ?: "День не выбран"
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                ) {
                    Text(
                        text = compactText,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun JournalEntryCard(
    entry: MoodEntry,
    onClick: () -> Unit,
) {
    val emotionMeta = Emotion.defaultList.firstOrNull { it.text == entry.emotion }
    val accent = emotionAccentColor(entry.emotion)
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
            ) { onClick() },
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(accent.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center,
                ) {
                    emotionMeta?.let {
                        Icon(
                            painter = painterResource(id = it.iconRes),
                            contentDescription = it.text,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(28.dp),
                        )
                    } ?: Text("🙂")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = formatRelativeDate(entry.createdAt), style = MaterialTheme.typography.labelLarge)
                    Text(
                        text = entry.emotion,
                        color = accent,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    )
                }
                Text(
                    text = formatTime(entry.createdAt),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = entry.text ?: "Без заметки",
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(shape = RoundedCornerShape(12.dp), color = accent.copy(alpha = 0.2f)) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = accent,
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "${entry.intensity}/5", color = accent, style = MaterialTheme.typography.labelMedium)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                // TODO: Show factor chips when tags are saved in MoodEntry model.
            }
        }
    }
}

@Composable
private fun JournalSortSheet(
    expanded: Boolean,
    currentSort: JournalSortMode,
    onDismiss: () -> Unit,
    onSelect: (JournalSortMode) -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss,
    ) {
        JournalSortMode.values().forEach { mode ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = when (mode) {
                            JournalSortMode.NEWEST_FIRST -> "Сначала новые"
                            JournalSortMode.OLDEST_FIRST -> "Сначала старые"
                            JournalSortMode.INTENSITY_HIGH_FIRST -> "Сильные эмоции сверху"
                            JournalSortMode.INTENSITY_LOW_FIRST -> "Слабые эмоции сверху"
                        } + if (mode == currentSort) " ✓" else "",
                    )
                },
                onClick = { onSelect(mode) },
            )
        }
    }
}

@Composable
private fun JournalEmptyState(
    title: String,
    subtitle: String,
    actionText: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAction, shape = RoundedCornerShape(999.dp)) {
            Text(actionText)
        }
    }
}

private data class CalendarDayUi(
    val year: Int,
    val month: Int,
    val day: Int,
    val weekday: String,
)

private fun buildWeekDays(startMillis: Long): List<CalendarDayUi> {
    val calendar = Calendar.getInstance().apply { timeInMillis = startMillis }
    val weekdays = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
    return weekdays.mapIndexed { index, dayLabel ->
        if (index > 0) calendar.add(Calendar.DAY_OF_MONTH, 1)
        CalendarDayUi(
            year = calendar.get(Calendar.YEAR),
            month = calendar.get(Calendar.MONTH),
            day = calendar.get(Calendar.DAY_OF_MONTH),
            weekday = dayLabel,
        )
    }
}

private fun dateKeyFromTimestamp(timestamp: Long): JournalDateKey {
    val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
    return JournalDateKey(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH),
        day = calendar.get(Calendar.DAY_OF_MONTH),
    )
}

private fun emotionAccentColor(emotion: String): Color = when (emotion) {
    "Радость" -> Color(0xFFE8B54B)
    "Спокойствие" -> Color(0xFF7FC9B3)
    "Грусть" -> Color(0xFF7EA7D8)
    "Тревога" -> Color(0xFFB7A7D9)
    "Злость" -> Color(0xFFD5897A)
    "Усталость" -> Color(0xFF9B8FB8)
    else -> Color(0xFFB29A7A)
}

private fun formatTime(timestamp: Long): String {
    val formatter = SimpleDateFormat("HH:mm", Locale("ru"))
    return formatter.format(Date(timestamp))
}

private fun formatRelativeDate(timestamp: Long): String {
    val target = Calendar.getInstance().apply { timeInMillis = timestamp }
    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -1) }
    return when {
        isSameDay(target, today) -> "Сегодня, ${target.get(Calendar.DAY_OF_MONTH)} ${monthName(target)}"
        isSameDay(target, yesterday) -> "Вчера, ${target.get(Calendar.DAY_OF_MONTH)} ${monthName(target)}"
        else -> "${target.get(Calendar.DAY_OF_MONTH)} ${monthName(target)}, ${weekdayName(target)}"
    }
}

private fun isSameDay(left: Calendar, right: Calendar): Boolean {
    return left.get(Calendar.YEAR) == right.get(Calendar.YEAR) &&
        left.get(Calendar.DAY_OF_YEAR) == right.get(Calendar.DAY_OF_YEAR)
}

private fun monthName(calendar: Calendar): String {
    return SimpleDateFormat("LLLL", Locale("ru")).format(calendar.time).lowercase(Locale("ru"))
}

private fun weekdayName(calendar: Calendar): String {
    return SimpleDateFormat("EEEE", Locale("ru")).format(calendar.time).lowercase(Locale("ru"))
}

private fun monthNameForIndex(monthIndex: Int): String {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.MONTH, monthIndex)
    }
    return monthName(calendar)
}

private fun dominantEmotion(
    entries: List<MoodEntry>,
    day: JournalDateKey,
): String? {
    return entries
        .asSequence()
        .filter { dateKeyFromTimestamp(it.createdAt) == day }
        .groupingBy { it.emotion }
        .eachCount()
        .maxByOrNull { it.value }
        ?.key
}

private fun weekdayShortByDate(date: JournalDateKey): String {
    val calendar = Calendar.getInstance().apply {
        set(Calendar.YEAR, date.year)
        set(Calendar.MONTH, date.month)
        set(Calendar.DAY_OF_MONTH, date.day)
    }
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> "Пн"
        Calendar.TUESDAY -> "Вт"
        Calendar.WEDNESDAY -> "Ср"
        Calendar.THURSDAY -> "Чт"
        Calendar.FRIDAY -> "Пт"
        Calendar.SATURDAY -> "Сб"
        Calendar.SUNDAY -> "Вс"
        else -> ""
    }
}

private fun todayDateKey(): JournalDateKey {
    val calendar = Calendar.getInstance()
    return JournalDateKey(
        year = calendar.get(Calendar.YEAR),
        month = calendar.get(Calendar.MONTH),
        day = calendar.get(Calendar.DAY_OF_MONTH),
    )
}