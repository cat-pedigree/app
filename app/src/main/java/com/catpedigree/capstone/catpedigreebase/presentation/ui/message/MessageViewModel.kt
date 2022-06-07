package com.catpedigree.capstone.catpedigreebase.presentation.ui.message

import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.MessageRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.RoomMessageRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository

class MessageViewModel(
    private val userRepository: UserRepository,
    private val roomRepository: RoomMessageRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    val userItems = userRepository.userItems.asLiveData()

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getMessageRoom() = userItems.switchMap {
        roomRepository.getMessageRoom(
            it.token ?: "",
            it.id!!
        )
    }
    
    fun getMessage(room_id:Int) = userItems.switchMap {
        messageRepository.getMessage(
            it.token ?: "",
            room_id
        )
    }
}