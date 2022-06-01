package com.catpedigree.capstone.catpedigreebase.presentation.ui.veterinary

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.CommentRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.VeterinaryRepository

class VeterinaryViewModel(userRepository: UserRepository, private val veterinaryRepository: VeterinaryRepository) : ViewModel() {

    val userItem = userRepository.userItems.asLiveData()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getVeterinary() = userItem.switchMap {
        veterinaryRepository.getVeterinary(
            it.token ?: "",
        )
    }
}