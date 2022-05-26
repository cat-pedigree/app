package com.catpedigree.capstone.catpedigreebase.presentation.ui.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems
import com.catpedigree.capstone.catpedigreebase.data.local.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.error.AuthError
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    val userItems = userRepository.userItems.asLiveData()
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getPosts() = userItems.switchMap {
        postRepository.getPosts(it.token ?: "")
    }

    fun savePost(post: PostItems){
        viewModelScope.launch {
            postRepository.setPostBookmark(post, true)
        }
    }

    fun deletePost(post: PostItems){
        viewModelScope.launch {
            postRepository.setPostBookmark(post, false)
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.logout()
            } catch (e: AuthError) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}