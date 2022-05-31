package com.catpedigree.capstone.catpedigreebase.presentation.ui.account

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.error.AuthError
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AccountViewModel(private val userRepository: UserRepository) : ViewModel() {

    var userItems = userRepository.userItems.asLiveData()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun changeEmail(
        token: String,
        email: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.changeEmail(token, email)
                _isSuccess.value = true
            } catch (e: PostError) {
                _errorMessage.value = e.message
                _isSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun changePassword(
        token: String,
        password: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.changePassword(token, password)
                _isSuccess.value = true
            } catch (e: PostError) {
                _errorMessage.value = e.message
                _isSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun userDelete(
        token: String,
        id: Int
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                userRepository.userDelete(token, id)
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