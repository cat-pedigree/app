package com.catpedigree.capstone.catpedigreebase.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.catpedigree.capstone.catpedigreebase.data.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.repository.UserRepository
import com.catpedigree.capstone.catpedigreebase.injection.Injection
import com.catpedigree.capstone.catpedigreebase.ui.auth.LoginViewModel
import com.catpedigree.capstone.catpedigreebase.ui.auth.RegisterViewModel
import com.catpedigree.capstone.catpedigreebase.ui.home.HomeViewModel

class ViewModelFactory(private val userRepository: UserRepository, private val postRepository: PostRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(userRepository, postRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideUserRepository(context), Injection.providePostRepository(context))
            }.also { instance = it }
    }
}