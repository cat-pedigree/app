package com.catpedigree.capstone.catpedigreebase.presentation.ui.post.create

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CreatePostViewModel(userRepository: UserRepository, private val postRepository: PostRepository) : ViewModel() {

    val userItems = userRepository.userItems.asLiveData()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun uploadPost(
        token: String,
        photo: MultipartBody.Part,
        title : String,
        description: RequestBody,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                postRepository.postCreate(token, photo, title, description)
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