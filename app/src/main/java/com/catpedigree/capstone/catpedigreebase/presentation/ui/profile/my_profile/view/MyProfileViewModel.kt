package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.view

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository

class MyProfileViewModel(
    userRepository: UserRepository,
    private val postRepository: PostRepository) : ViewModel() {

    val userItems = userRepository.userItems.asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getPostProfile() = userItems.switchMap {
        postRepository.getPostsProfile(
            it.token ?: "",
            it.id!!
        )
    }
}