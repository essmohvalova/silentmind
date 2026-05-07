@file:OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)

package com.example.coursework_app.ui.practice.breathing.picker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.coursework_app.ui.practice.BreathingTechnique
import kotlinx.coroutines.launch

@Composable
fun BreathingTechniquePickerScreen(
    onBackClick: () -> Unit,
    onTechniqueClick: (BreathingTechnique, Int) -> Unit,
) {
    val techniques = BreathingTechnique.entries
    val pageCount = Int.MAX_VALUE
    val startPage = pageCount / 2 - (pageCount / 2) % techniques.size
    val pagerState = rememberPagerState(initialPage = startPage) { pageCount }
    val scope = rememberCoroutineScope()
    var selectedCycles by remember { mutableIntStateOf(1) }
    val selectedTechnique = techniques[pagerState.currentPage % techniques.size]
    val palette = selectedTechnique.palette()

    Column(modifier = androidx.compose.ui.Modifier.fillMaxSize()) {
        PickerTopBar(
            title = "Дыхание",
            techniqueName = selectedTechnique.title,
            accent = palette.accentDark,
            onBackClick = onBackClick,
            onPreviousClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
            onNextClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
        )
        HorizontalPager(
            state = pagerState,
            modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        ) { page ->
            val technique = techniques[page % techniques.size]
            TechniquePickerPage(
                technique = technique,
                selectedCycles = selectedCycles,
                palette = technique.palette(),
                onSelectCycles = { selectedCycles = it },
                onStartClick = { onTechniqueClick(technique, selectedCycles) },
            )
        }
    }
}
