package com.catpedigree.capstone.catpedigreebase.utils.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.catpedigree.capstone.catpedigreebase.data.network.api.config.ApiConfig
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.local.preferences.SharedPrefUserLogin
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.CommentRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.PostRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.UserRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.repository.CommentRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.PostRepository
import com.catpedigree.capstone.catpedigreebase.data.local.repository.UserRepository

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