package com.catpedigree.capstone.catpedigreebase.ui.comment

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.catpedigree.capstone.catpedigreebase.api.apiinterface.CommentInterface
import com.catpedigree.capstone.catpedigreebase.api.config.ApiConfig
import com.catpedigree.capstone.catpedigreebase.data.item.CommentItems
import com.catpedigree.capstone.catpedigreebase.data.item.PostItems
import com.catpedigree.capstone.catpedigreebase.data.repository.CommentRepository
import com.catpedigree.capstone.catpedigreebase.data.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.CommentError
import com.catpedigree.capstone.catpedigreebase.utils.PostError
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CommentViewModel(userRepository: UserRepository, private val commentRepository: CommentRepository) : ViewModel() {

    val userItem = userRepository.userItems.asLiveData()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun comments(post_id: Int) : LiveData<PagingData<CommentItems>>{
            val getComment : LiveData<PagingData<CommentItems>> =
            userItem.switchMap {
                commentRepository.getComments(
                    it.token ?: "",
                    post_id
                ).cachedIn(viewModelScope)
            }
            return getComment
        }


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