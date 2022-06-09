package com.catpedigree.capstone.catpedigreebase.presentation.ui.cat.add

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.CatRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.error.AuthError
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddCatViewModel(
    private val userRepository: UserRepository,
    private val catRepository: CatRepository
    ) : ViewModel() {

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
        eye_color: RequestBody,
        hair_color: RequestBody,
        ear_shape: RequestBody,
        weight: Double,
        age: Int,
        photo: MultipartBody.Part,
        isWhite: Int,
        story: RequestBody,
        latLng: LatLng? = null
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
                    eye_color,
                    hair_color,
                    ear_shape,
                    weight,
                    age,
                    photo,
                    isWhite,
                    story,
                    latLng
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