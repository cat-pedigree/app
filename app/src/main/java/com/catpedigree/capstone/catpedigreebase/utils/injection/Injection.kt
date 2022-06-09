package com.catpedigree.capstone.catpedigreebase.utils.injection

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.catpedigree.capstone.catpedigreebase.data.local.preferences.SharedPrefUserLogin
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.*
import com.catpedigree.capstone.catpedigreebase.data.local.repository.*
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.api.config.ApiConfig

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val userApiInterface = ApiConfig.getUserApi()
        val userRemoteDataSource = UserRemoteDataSource(userApiInterface)
        return UserRepository(
            context,
            userRemoteDataSource,
            SharedPrefUserLogin.getInstance(context.dataStore),
            CatDatabase.getDatabase(context)
        )
    }

    fun providePostRepository(context: Context): PostRepository {
        val postApiInterface = ApiConfig.getPostApi()
        val postRemoteDataSource = PostRemoteDataSource(postApiInterface)
        return PostRepository(postRemoteDataSource, CatDatabase.getDatabase(context))
    }

    fun provideCommentRepository(context: Context): CommentRepository {
        val commentApiInterface = ApiConfig.getCommentApi()
        val commentRemoteDataSource = CommentRemoteDataSource(commentApiInterface)
        return CommentRepository(commentRemoteDataSource, CatDatabase.getDatabase(context))
    }

    fun provideCatRepository(context: Context): CatRepository {
        val catApiInterface = ApiConfig.getCatApi()
        val catRemoteDataSource = CatRemoteDataSource(catApiInterface)
        return CatRepository(catRemoteDataSource, CatDatabase.getDatabase(context))
    }

    fun provideVeterinaryRepository(context: Context): VeterinaryRepository {
        val veterinaryApiInterface = ApiConfig.getVeterinary()
        val veterinaryRemoteDataSource = VeterinaryRemoteDataSource(veterinaryApiInterface)
        return VeterinaryRepository(veterinaryRemoteDataSource, CatDatabase.getDatabase(context))
    }

    fun provideFollowRepository(context: Context): FollowRepository{
        val followApiInterface = ApiConfig.getFollow()
        val followRemoteDataSource = FollowRemoteDataSource(followApiInterface)
        return FollowRepository(followRemoteDataSource, CatDatabase.getDatabase(context))
    }
}