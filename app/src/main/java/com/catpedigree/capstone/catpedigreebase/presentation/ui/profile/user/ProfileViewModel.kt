package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.user

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.CatRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
import kotlinx.coroutines.launch

class ProfileViewModel(

    private val userRepository: UserRepository,
    private val catRepository: CatRepository,
    private val postRepository: PostRepository
) : ViewModel() {

    val userItems = userRepository.userItems.asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getCat(user_id: Int) = userItems.switchMap {
        catRepository.getCat(
            it.token ?: "",
            user_id
        )
    }

    fun getPostProfile(user_id: Int) = userItems.switchMap {
        postRepository.getPostsProfile(
            it.token ?: "",
            user_id
        )
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
}