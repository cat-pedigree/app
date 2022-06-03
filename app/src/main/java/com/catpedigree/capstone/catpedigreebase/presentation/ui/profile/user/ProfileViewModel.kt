package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.user

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.CatRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.FollowRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserDataItems
import com.catpedigree.capstone.catpedigreebase.utils.error.FollowError
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
import kotlinx.coroutines.launch

class ProfileViewModel(

    private val userRepository: UserRepository,
    private val catRepository: CatRepository,
    private val postRepository: PostRepository,
    private val followRepository: FollowRepository
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

    fun checkCat() = userItems.switchMap {
        catRepository.checkCat(it.id!!)
    }

    fun getPostProfile(user_id: Int) = userItems.switchMap {
        postRepository.getPostsProfile(
            it.token ?: "",
            user_id
        )
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

    fun getFollower(user_id: Int) = userItems.switchMap {
        followRepository.getFollow(it.token ?: "", user_id)
    }

    fun getFollowing(follower_id: Int) = userItems.switchMap {
        followRepository.getFollowing(it.token ?: "", follower_id)
    }

    fun checkFollow(user_id: Int) = userItems.switchMap {
        it.id?.let { it1 -> followRepository.checkFollow(user_id, it1) }!!
    }

    fun follow(
        token: String,
        user_id: Int,
        follower_id: Int
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                followRepository.postFollow(token, user_id, follower_id)
                _isSuccess.value = true
            } catch (e: FollowError) {
                _errorMessage.value = e.message
                _isSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveFollow(user: UserDataItems) {
        viewModelScope.launch {
            userRepository.setFollowersCount(user, 1)
        }
    }

    fun deleteFollow(user: UserDataItems) {
        viewModelScope.launch {
            userRepository.setFollowersCountDelete(user, 1)
        }
    }

    fun getFollowersCount(id: Int): LiveData<Int> = userRepository.getFollowerCount(id)

    fun getCheckFollow(follower_id: Int): LiveData<Int> = userItems.switchMap {
        followRepository.getCheckFollow(it.id!!, follower_id)
    }

    fun followDelete(
        token: String,
        user_id: Int,
        follower_id: Int,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                followRepository.followDelete(token,user_id, follower_id)
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