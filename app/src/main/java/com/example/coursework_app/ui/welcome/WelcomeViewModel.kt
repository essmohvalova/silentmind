package com.example.coursework_app.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.domain.usecase.IsUserAuthorizedUseCase
import com.example.coursework_app.utils.runSuspendCatching
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val isUserAuthorizedUseCase: IsUserAuthorizedUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(WelcomeState())
    val uiState: StateFlow<WelcomeState> = _state.asStateFlow()

    init {
        checkUser()
    }

    private fun checkUser() {
        viewModelScope.launch {
            runSuspendCatching {
                val isAuthorized = isUserAuthorizedUseCase()

                delay(2000)

                _state.update {
                    it.copy(
                        isLoading = false,
                        destination = if (isAuthorized) {
                            WelcomeState.Destination.MAIN
                        } else {
                            WelcomeState.Destination.ONBOARDING
                        }
                    )
                }
            }
        }
    }
}

