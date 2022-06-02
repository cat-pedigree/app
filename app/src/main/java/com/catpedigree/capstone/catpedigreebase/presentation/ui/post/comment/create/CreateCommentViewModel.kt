package com.catpedigree.capstone.catpedigreebase.presentation.ui.post.comment.create

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.CommentRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.error.CommentError
import kotlinx.coroutines.launch

class CreateCommentViewModel(
    userRepository: UserRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    val userItem = userRepository.userItems.asLiveData()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun createComment(
        token: String,
        post_id: Int,
        user_id: Int,
        description: String,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                commentRepository.commentCreate(token, post_id, user_id, description)
                _isSuccess.value = true
            } catch (e: CommentError) {
                _errorMessage.value = e.message
                _isSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }
}