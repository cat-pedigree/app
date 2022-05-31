package com.catpedigree.capstone.catpedigreebase.data.local.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.local.preferences.SharedPrefUserLogin
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.UserRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserDataItems
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.error.AuthError
import com.catpedigree.capstone.catpedigreebase.utils.error.PostError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

open class UserRepository(
    private val context: Context,
    private val userRemote: UserRemoteDataSource,
    private val prefs: SharedPrefUserLogin,
    private val catDatabase: CatDatabase
) {

    val userItems = prefs.getUser()

    suspend fun login(email: String, password: String) {
        try {
            val responses = userRemote.login(email, password)
            if (responses.isSuccessful && responses.body() != null) {
                val result = responses.body()?.data
                result?.let {
                    val user = UserItems(
                        id = it.user?.id,
                        name = it.user?.name,
                        username = it.user?.username,
                        phone_number = it.user?.phone_number,
                        email = email,
                        bio = it.user?.bio,
                        lat = it.user?.lat,
                        lon = it.user?.lon,
                        profile_photo_path = it.user?.profile_photo_path,
                        token = it.token,
                        isLoggedIn = true,
                        postsCount = it.user?.posts_count,
                        catsCount = it.user?.cats_count
                    )
                    prefs.updateUser(user)
                }
            } else {
                throw AuthError(context.getString(R.string.error_login))
            }
        } catch (e: Throwable) {
            throw AuthError(e.message.toString())
        }
    }

    suspend fun register(name: String, username: String, phone_number: String, email: String, password: String) {
        try {
            val responses = userRemote.register(name, username, phone_number, email, password)
            if (!responses.isSuccessful) {
                throw AuthError(
                    responses.body()?.message ?: context.getString(R.string.register_error)
                )
            }
        } catch (e: Throwable) {
            throw AuthError(e.message.toString())
        }
    }

    fun search(token: String, name: String): LiveData<Result<List<UserDataItems>>> = liveData {
        emit(Result.Loading)
        try{
            val response = userRemote.search(token, name)
            val users = response.body()?.data
            val newUser = users?.map {user ->
                UserDataItems(
                    user.id,
                    user.name,
                    user.username,
                    user.bio,
                    user.lat,
                    user.lon,
                    user.profile_photo_path,
                    user.posts_count,
                    user.cats_count
                )
            }
            catDatabase.userDao().deleteAllUsers()
            catDatabase.userDao().insertUser(newUser!!)
        }catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<UserDataItems>>> = catDatabase.userDao().getSearch(name).map { Result.Success(it) }
        emitSource(dataLocal)
    }

    fun getUser(id: Int): LiveData<List<UserDataItems>>{
        return catDatabase.userDao().getUser(id)
    }

    suspend fun change(
        token: String,
        profile_photo_path: MultipartBody.Part,
        slug: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    userRemote.change(
                        token,
                        profile_photo_path,
                    )
                prefs.updateUser(UserItems(profile_photo_path = slug))
                if (!response.isSuccessful) {
                    throw PostError(response.message())
                }
            } catch (e: Throwable) {
                throw PostError(e.message.toString())
            }
        }
    }

    suspend fun changeEmail(
        token: String,
        email: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    userRemote.changeEmail(
                        token,
                        email,
                    )
                prefs.updateUser(UserItems(email = email))
                if (!response.isSuccessful) {
                    throw PostError(response.message())
                }
            } catch (e: Throwable) {
                throw PostError(e.message.toString())
            }
        }
    }

    suspend fun changePassword(
        token: String,
        password: String
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    userRemote.changePassword(
                        token,
                        password,
                    )
                if (!response.isSuccessful) {
                    throw PostError(response.message())
                }
            } catch (e: Throwable) {
                throw PostError(e.message.toString())
            }
        }
    }

    suspend fun userDelete(
        token: String,
        id: Int
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    userRemote.userDelete(
                        token,
                        id
                    )

                if (!response.isSuccessful) {
                    throw PostError(response.message())
                }
            } catch (e: Throwable) {
                throw PostError(e.message.toString())
            }
        }
    }

    suspend fun profile(
        token: String,
        name: String,
        username: String,
        bio:String
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    userRemote.profile(
                        token,
                        name,
                        username,
                        bio
                    )

                prefs.updateUser(UserItems(name = name, username = username, bio = bio))
                if (!response.isSuccessful) {
                    throw PostError(response.message())
                }
            } catch (e: Throwable) {
                throw PostError(e.message.toString())
            }
        }
    }

    suspend fun logout() {
        try {
            prefs.updateUser(UserItems(
                token = null,
                isLoggedIn = false,
                id = null,
                name = null,
                username = null,
                phone_number = null,
                email = null,
                bio = null,
                lat = null,
                lon = null,
                profile_photo_path = "",
                postsCount = null,
                catsCount = null
            ))
        } catch (e: Throwable) {
            throw AuthError(e.message.toString())
        }
    }
}