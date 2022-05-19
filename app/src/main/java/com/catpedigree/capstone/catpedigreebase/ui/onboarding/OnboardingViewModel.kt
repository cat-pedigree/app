package com.catpedigree.capstone.catpedigreebase.ui.onboarding

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.AuthError
import kotlinx.coroutines.launch

class OnboardingViewModel(private val userRepository: UserRepository) : ViewModel() {

    var userItem = userRepository.userItems.asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                userRepository.login(email, password)
            } catch (e: AuthError) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}