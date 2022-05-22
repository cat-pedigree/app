package com.catpedigree.capstone.catpedigreebase.ui.maps

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.repository.UserRepository
import kotlinx.coroutines.launch

class MapsViewModel(userRepository: UserRepository) :
    ViewModel() {

    val userItems = userRepository.userItems.asLiveData()

//    private val _stories = MutableLiveData<List<StoryItems>>()
//    val stories: LiveData<List<StoryItems>> = _stories

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

//    fun getStoriesWithLocation(token: String) {
//        viewModelScope.launch {
//            try {
//                val stories = storyRepository.getStoriesWithLocation(token = token)
//                _stories.value = stories
//            } catch (e: StoryError) {
//                _errorMessage.value = e.message.toString()
//            }
//        }
//    }

}