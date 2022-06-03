package com.catpedigree.capstone.catpedigreebase.presentation.ui.home

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems
import com.catpedigree.capstone.catpedigreebase.utils.error.AuthError
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
) : ViewModel() {

    val userItems = userRepository.userItems.asLiveData()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getPosts() = userItems.switchMap {
        postRepository.getPosts(it.token ?: "")
    }

    fun search(name: String) = userItems.switchMap {
        userRepository.search(it.token ?: "", name)
    }

    fun checkPost() = userItems.switchMap {
        postRepository.checkPost(it.id!!)
    }

    fun savePost(post: PostItems) {
        viewModelScope.launch {
            postRepository.setPostBookmark(post, true)
        }
    }

    fun deletePost(post: PostItems) {
        viewModelScope.launch {
            postRepository.setPostBookmark(post, false)
        }
    }

    fun createLovePost(post: PostItems) {
        viewModelScope.launch {
            postRepository.setPostLove(post, true)
        }
    }

    fun deleteLovePost(post: PostItems) {
        viewModelScope.launch {
            postRepository.setPostLove(post, false)
        }
    }

    fun loveCreate(
        token: String,
        post_id: Int,
        user_id: Int
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                postRepository.loveCreate(token, post_id, user_id)
                _isSuccess.value = true
            } catch (e: PostError) {
                _errorMessage.value = e.message
                _isSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loveDelete(
        token: String,
        post_id: Int,
        user_id: Int,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                postRepository.loveDelete(token, post_id, user_id)
                _isSuccess.value = true
            } catch (e: PostError) {
                _errorMessage.value = e.message
                _isSuccess.value = false
            } finally {
                _isLoading.value = false
            }
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