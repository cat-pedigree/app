package com.catpedigree.capstone.catpedigreebase.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.catpedigree.capstone.catpedigreebase.api.config.ApiConfig
import com.catpedigree.capstone.catpedigreebase.data.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.preferences.SharedPrefUserLogin
import com.catpedigree.capstone.catpedigreebase.data.remote.CommentRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.remote.PostRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.remote.UserRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.repository.CommentRepository
import com.catpedigree.capstone.catpedigreebase.data.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.repository.UserRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val userApiInterface = ApiConfig.getUserApi()
        val userRemoteDataSource = UserRemoteDataSource(userApiInterface)
        return UserRepository(context, userRemoteDataSource, SharedPrefUserLogin.getInstance(context.dataStore))
    }

    fun providePostRepository(context: Context): PostRepository {
        val postApiInterface = ApiConfig.getPostApi()
        val postRemoteDataSource = PostRemoteDataSource(postApiInterface)
        return PostRepository(postRemoteDataSource, CatDatabase.getDatabase(context))
    }

    fun provideCommentRepository(context: Context): CommentRepository{
        val commentApiInterface = ApiConfig.getCommentApi()
        val commentRemoteDataSource = CommentRemoteDataSource(commentApiInterface)
        return CommentRepository(commentRemoteDataSource, CatDatabase.getDatabase(context))
    }

}