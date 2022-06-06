package com.catpedigree.capstone.catpedigreebase.presentation.ui.maps

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.CatRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.data.network.item.CatItems
import com.catpedigree.capstone.catpedigreebase.utils.error.CatError
import kotlinx.coroutines.launch

class MapsViewModel(
    private val userRepository: UserRepository,
    private val catRepository: CatRepository
) :
    ViewModel() {

    val userItems = userRepository.userItems.asLiveData()

    private val _cats = MutableLiveData<List<CatItems>>()
    val cats: LiveData<List<CatItems>> = _cats

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getCatLocation(token: String) {
        viewModelScope.launch {
            try {
                val cats = catRepository.getCatLocation(token = token)
                _cats.value = cats
            } catch (e: CatError) {
                _errorMessage.value = e.message.toString()
            }
        }
    }
}