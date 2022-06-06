package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.view

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.CatRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.FollowRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.error.AuthError
import kotlinx.coroutines.launch

class MyProfileViewModel(

    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val catRepository: CatRepository,
    private val followRepository: FollowRepository
) : ViewModel() {

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

    fun getMyFollower() = userItems.switchMap {
        followRepository.getFollow(it.token ?: "", it.id!!)
    }

    fun getMyFollowing() = userItems.switchMap {
        followRepository.getFollowing(it.token ?: "", it.id!!)
    }

    fun getCat() = userItems.switchMap {
        catRepository.getCat(
            it.token ?: "",
            it.id!!
        )
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