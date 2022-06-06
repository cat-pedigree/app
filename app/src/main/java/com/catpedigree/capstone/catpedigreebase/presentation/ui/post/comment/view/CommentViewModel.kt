package com.catpedigree.capstone.catpedigreebase.presentation.ui.post.comment.view

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.CommentRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.error.AuthError
import kotlinx.coroutines.launch

class CommentViewModel(
    private val userRepository: UserRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    val userItem = userRepository.userItems.asLiveData()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun comments(post_id: Int) = userItem.switchMap {
        commentRepository.getComments(
            it.token ?: "",
            post_id
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