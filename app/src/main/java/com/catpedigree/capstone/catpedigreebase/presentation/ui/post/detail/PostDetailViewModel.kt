package com.catpedigree.capstone.catpedigreebase.presentation.ui.post.detail

import android.app.Application
import androidx.lifecycle.*
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.local.room.dao.FavoriteDao
import com.catpedigree.capstone.catpedigreebase.data.local.room.dao.LoveDao
import com.catpedigree.capstone.catpedigreebase.data.network.item.FavoriteItems
import com.catpedigree.capstone.catpedigreebase.data.network.item.LoveItems
import com.catpedigree.capstone.catpedigreebase.data.local.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.utils.error.AuthError
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PostDetailViewModel(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    application: Application,
) : AndroidViewModel(application) {

    val userItems = userRepository.userItems.asLiveData()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean> = _isSuccess

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val postDao: FavoriteDao?
    private val loveDao: LoveDao?

    init{
        val postDB = CatDatabase.getDatabase(application)
        postDao = postDB.favoriteDao()
        loveDao = postDB.loveDao()
    }

    fun addToFavoritePost(
        id: Int?,
        name:String?,
        profile_photo_path: String?,
        photo: String?,
        description: String?,
        loves_count: Int?,
        comments_count: Int?
    ){
        CoroutineScope(Dispatchers.IO).launch {
            val post = FavoriteItems(
                id,
                name,
                profile_photo_path,
                photo,
                description,
                loves_count,
                comments_count
            )
            postDao?.addToFavorite(post)
        }
    }

    suspend fun checkPost(id: Int) = postDao?.checkPost(id)


    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            postDao?.removeFromFavorite(id)
        }
    }

    fun loveCreate(
        token: String,
        post_id: Int,
        user_id: Int
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                postRepository.loveCreate(token, post_id, user_id)
                _isSuccess.value = true
            } catch (e: PostError) {
                _errorMessage.value = e.message
                _isSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addToLovePost(
        id: Int?,
        post_id: Int?,
        user_id: Int?
    ){
        CoroutineScope(Dispatchers.IO).launch {
            val post = LoveItems(
                id,
                post_id,
                user_id
            )
            loveDao?.insertLove(post)
        }
    }

    suspend fun checkLove(id: Int) = loveDao?.checkLove(id)


    fun removeFromLove(post_id: Int, user_id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            loveDao?.removeFromLove(post_id,user_id)
        }
    }

    fun loveDelete(
        token: String,
        post_id: Int,
        user_id: Int,
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                postRepository.loveDelete(token, post_id, user_id)
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