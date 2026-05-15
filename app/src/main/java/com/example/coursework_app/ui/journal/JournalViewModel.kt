package com.example.coursework_app.ui.journal



import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope

import com.example.coursework_app.domain.usecase.ObserveJournalMoodEntriesUseCase

import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.flow.StateFlow

import kotlinx.coroutines.launch

import javax.inject.Inject



@HiltViewModel

class JournalViewModel @Inject constructor(

    private val observeJournalMoodEntriesUseCase: ObserveJournalMoodEntriesUseCase,

    private val journalEntryUiFactory: JournalEntryUiFactory,

) : ViewModel() {



    private val _uiState = MutableStateFlow(JournalUiState())

    val uiState: StateFlow<JournalUiState> = _uiState



    init {

        viewModelScope.launch {

            observeJournalMoodEntriesUseCase().collect { domainEntries ->

                val entries = domainEntries.map { journalEntryUiFactory.fromDomain(it) }

                _uiState.value = JournalUiState(

                    entriesCount = entries.size,

                    entries = entries,

                    isLoading = false,

                )

            }

        }

    }

}

