package com.catpedigree.capstone.catpedigreebase.presentation.ui.services

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository

class ServicesViewModel(userRepository: UserRepository) : ViewModel() {

    val userItem = userRepository.userItems.asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
}