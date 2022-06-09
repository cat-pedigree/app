package com.catpedigree.capstone.catpedigreebase.presentation.ui.pedigree

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.CatRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.FollowRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.data.network.item.CatItems
import com.catpedigree.capstone.catpedigreebase.data.network.item.PostItems
import com.catpedigree.capstone.catpedigreebase.utils.error.AuthError
import kotlinx.coroutines.launch

class PedigreeViewModel(

    private val userRepository: UserRepository,
    private val catRepository: CatRepository,
) : ViewModel() {

    val userItems = userRepository.userItems.asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getCat() = userItems.switchMap {
        catRepository.getCat(
            it.token ?: "",
            it.id!!
        )
    }

    fun getCatFilter(
        breed: String?,
        color: String?,
        gender: String?,
        isWhite: Int?
    ) = userItems.switchMap {
        catRepository.getCatFilter(
            it.token ?: "",
            breed?: "",
            color ?: "",
            gender ?: "",
            isWhite!!,
        )
    }

    fun createSelectedCat(cat: CatItems) {
        viewModelScope.launch {
            catRepository.setCatSelected(cat, true)
        }
    }

    fun deleteSelectedCat(cat: CatItems) {
        viewModelScope.launch {
            catRepository.setCatSelected(cat, false)
        }
    }

    fun getCatSelected() = catRepository.getCatSelected()

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