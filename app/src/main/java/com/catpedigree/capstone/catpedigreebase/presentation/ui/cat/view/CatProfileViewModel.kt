package com.catpedigree.capstone.catpedigreebase.presentation.ui.cat.view

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.CatRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.error.CatError
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class CatProfileViewModel(
    userRepository: UserRepository,
    private val catRepository: CatRepository
) : ViewModel() {

    val userItem = userRepository.userItems.asLiveData()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun createAlbum(
        token: String,
        user_id: Int,
        cat_id: Int,
        photo: MultipartBody.Part,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                catRepository.catCreateAlbum(token, user_id, cat_id, photo)
                _isSuccess.value = true
            } catch (e: CatError) {
                _errorMessage.value = e.message
                _isSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getAlbum(cat_id: Int) = userItem.switchMap {
        catRepository.getAlbum(
            it.token ?: "",
            it.id!!,
            cat_id
        )
    }
}