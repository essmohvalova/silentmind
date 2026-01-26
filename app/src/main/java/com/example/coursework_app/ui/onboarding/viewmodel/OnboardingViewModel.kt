package com.example.coursework_app.ui.onboarding.viewmodel


import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.coursework_app.data.repository.UserRepository
import com.example.coursework_app.domain.model.CharacterType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Используем AndroidViewModel для доступа к Context
class OnboardingViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application.applicationContext)

    private val _selectedCharacter = MutableStateFlow(CharacterType.CAT)
    val selectedCharacter: StateFlow<CharacterType> = _selectedCharacter

    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName

    fun selectCharacter(character: CharacterType) {
        _selectedCharacter.value = character
    }

    fun updateUserName(name: String) {
        _userName.value = name
    }

    fun saveUserData() {
        viewModelScope.launch {
            // Просто логируем для теста
            println("Сохранение пользователя: ${userName.value}, ${selectedCharacter.value}")
            // userRepository.saveUser(...) // Раскомментировать позже
        }
    }
}