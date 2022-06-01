package com.catpedigree.capstone.catpedigreebase.presentation.ui.cat.add

import android.text.Editable
import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.CatRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddCatViewModel(userRepository: UserRepository, private val catRepository: CatRepository) :
    ViewModel() {

    val userItems = userRepository.userItems.asLiveData()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun uploadCat(
        token: String,
        user_id: Int,
        name: RequestBody,
        breed: RequestBody,
        gender: RequestBody,
        color: RequestBody,
        weight: Double,
        age: Int,
        story: RequestBody,
        photo: MultipartBody.Part,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                catRepository.catCreate(
                    token,
                    user_id,
                    name,
                    breed,
                    gender,
                    color,
                    weight,
                    age,
                    story,
                    photo
                )
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