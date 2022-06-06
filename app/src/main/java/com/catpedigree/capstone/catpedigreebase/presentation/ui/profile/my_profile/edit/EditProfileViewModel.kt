package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.edit

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.error.AuthError
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    val userItems = userRepository.userItems.asLiveData()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun change(
        token: String,
        profile_photo_path: MultipartBody.Part,
        slug: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.change(token, profile_photo_path, slug)

                _isSuccess.value = true
            } catch (e: PostError) {
                _errorMessage.value = e.message
                _isSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun profile(
        token: String,
        name: String,
        username: String,
        bio: String,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.profile(token, name, username, bio)
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