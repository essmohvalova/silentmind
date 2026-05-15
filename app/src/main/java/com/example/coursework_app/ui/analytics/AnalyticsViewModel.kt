package com.example.coursework_app.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.model.analytics.AnalyticsPeriod
import com.example.coursework_app.domain.model.analytics.AnalyticsSummary
import com.example.coursework_app.domain.usecase.GetAnalyticsUserIdUseCase
import com.example.coursework_app.domain.usecase.ObserveAnalyticsSummaryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class AnalyticsViewModel @Inject constructor(
    private val observeAnalyticsSummaryUseCase: ObserveAnalyticsSummaryUseCase,
    private val getAnalyticsUserIdUseCase: GetAnalyticsUserIdUseCase,
) : ViewModel() {

    private val selectedPeriod = MutableStateFlow(AnalyticsPeriod.WEEK)
    private val monthOffset = MutableStateFlow(0)

    private val userIdFlow = MutableStateFlow<String?>(null)

    val uiState: StateFlow<AnalyticsUiState> = combine(
        selectedPeriod,
        monthOffset,
        userIdFlow,
    ) { period, month, userId ->
        Triple(period, month, userId)
    }.flatMapLatest { (period, month, userId) ->
        if (userId == null) {
            emptyFlow<AnalyticsUiState>()
        } else {
            observeAnalyticsSummaryUseCase(userId, period, month).combine(selectedPeriod) { summary, selected ->
                AnalyticsUiState(
                    isLoading = false,
                    period = selected,
                    monthOffset = month,
                    summary = summary,
                    isEmpty = summary.filteredEntries.isEmpty(),
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AnalyticsUiState(isLoading = true),
    )

    init {
        viewModelScope.launch {
            userIdFlow.value = getAnalyticsUserIdUseCase()
        }
    }

    fun onSelectPeriod(period: AnalyticsPeriod) {
        selectedPeriod.value = period
    }

    fun onPreviousMonth() {
        monthOffset.update { it - 1 }
    }

    fun onNextMonth() {
        monthOffset.update { if (it < 0) it + 1 else 0 }
    }
}

data class AnalyticsUiState(
    val isLoading: Boolean = false,
    val isEmpty: Boolean = true,
    val period: AnalyticsPeriod = AnalyticsPeriod.WEEK,
    val monthOffset: Int = 0,
    val summary: AnalyticsSummary? = null,
)
