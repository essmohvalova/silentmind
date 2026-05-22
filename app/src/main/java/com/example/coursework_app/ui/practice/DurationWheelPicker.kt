@file:OptIn(ExperimentalFoundationApi::class)

package com.example.coursework_app.ui.practice

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlin.math.abs

private const val MAX_MINUTES = 10
private const val MAX_TOTAL_SECONDS = MAX_MINUTES * 60 + 59

fun clampPracticeDurationSeconds(seconds: Int): Int =
    seconds.coerceIn(0, MAX_TOTAL_SECONDS)

private fun LazyListState.snappedItemIndex(itemCount: Int, centerShiftPx: Float): Int {
    if (itemCount <= 0) return 0
    val info = layoutInfo
    if (info.visibleItemsInfo.isEmpty()) return 0
    val viewportCenter = info.viewportStartOffset + info.viewportSize.height / 2f + centerShiftPx
    return info.visibleItemsInfo
        .minByOrNull { abs((it.offset + it.size / 2f) - viewportCenter) }
        ?.index
        ?.coerceIn(0, itemCount - 1) ?: 0
}

@Composable
fun DurationWheelPicker(
    totalSeconds: Int,
    onTotalSecondsChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    itemHeight: Dp = 52.dp,
    visibleHeight: Dp = 200.dp,
    selectionOffsetY: Dp = 10.dp,
) {
    val initial = remember {
        val clamped = clampPracticeDurationSeconds(totalSeconds)
        val m = (clamped / 60).coerceIn(0, MAX_MINUTES)
        val s = (clamped % 60).coerceIn(0, 59)
        m to s
    }
    val minuteCount = MAX_MINUTES + 1
    val secondCount = 60

    val minuteState = rememberLazyListState(initialFirstVisibleItemIndex = initial.first)
    val secondState = rememberLazyListState(initialFirstVisibleItemIndex = initial.second)
    val minuteSnap = rememberSnapFlingBehavior(lazyListState = minuteState)
    val secondSnap = rememberSnapFlingBehavior(lazyListState = secondState)
    val centerShiftPx = with(androidx.compose.ui.platform.LocalDensity.current) {
        selectionOffsetY.toPx()
    }

    var displayMinute by remember { mutableIntStateOf(initial.first) }
    var displaySecond by remember { mutableIntStateOf(initial.second) }

    LaunchedEffect(minuteState, secondState) {
        snapshotFlow {
            if (minuteState.layoutInfo.visibleItemsInfo.isEmpty() ||
                secondState.layoutInfo.visibleItemsInfo.isEmpty()
            ) {
                null
            } else {
                minuteState.snappedItemIndex(
                    itemCount = minuteCount,
                    centerShiftPx = centerShiftPx,
                ) to secondState.snappedItemIndex(
                    itemCount = secondCount,
                    centerShiftPx = centerShiftPx,
                )
            }
        }
            .filterNotNull()
            .distinctUntilChanged()
            .collect { (m, s) ->
                displayMinute = m
                displaySecond = s
                onTotalSecondsChange(clampPracticeDurationSeconds(m * 60 + s))
            }
    }

    val topPad = (visibleHeight - itemHeight) / 2

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = formatDurationMmSs(displayMinute, displaySecond),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(12.dp),
                    )
                    .padding(vertical = 14.dp, horizontal = 16.dp),
            )

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(visibleHeight),
            ) {
                val highlightH = itemHeight
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .height(highlightH)
                        .align(Alignment.Center)
                        .offset(y = selectionOffsetY)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)),
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    WheelColumn(
                        label = "мин",
                        itemCount = minuteCount,
                        listState = minuteState,
                        flingBehavior = minuteSnap,
                        itemHeight = itemHeight,
                        topPad = topPad,
                        selectedIndex = displayMinute,
                        formatValue = { "%02d".format(it) },
                        modifier = Modifier.width(120.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    WheelColumn(
                        label = "сек",
                        itemCount = secondCount,
                        listState = secondState,
                        flingBehavior = secondSnap,
                        itemHeight = itemHeight,
                        topPad = topPad,
                        selectedIndex = displaySecond,
                        formatValue = { "%02d".format(it) },
                        modifier = Modifier.width(120.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun WheelColumn(
    label: String,
    itemCount: Int,
    listState: LazyListState,
    flingBehavior: FlingBehavior,
    itemHeight: Dp,
    topPad: Dp,
    selectedIndex: Int,
    formatValue: (Int) -> String,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
        )
        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = topPad),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(itemCount) { index ->
                val distance = abs(index - selectedIndex).toFloat().coerceAtMost(4f)
                val alpha = (1f - distance * 0.22f).coerceIn(0.22f, 1f)
                val scale = (1f - distance * 0.06f).coerceIn(0.82f, 1f)
                val isCenter = index == selectedIndex
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = formatValue(index),
                        style = if (isCenter) {
                            MaterialTheme.typography.headlineMedium
                        } else {
                            MaterialTheme.typography.titleLarge
                        },
                        fontWeight = if (isCenter) FontWeight.SemiBold else FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            this.alpha = alpha
                        },
                    )
                }
            }
        }
    }
}

fun formatDurationMmSs(minutes: Int, seconds: Int): String =
    "%02d:%02d".format(minutes.coerceIn(0, MAX_MINUTES), seconds.coerceIn(0, 59))
