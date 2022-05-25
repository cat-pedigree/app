package com.catpedigree.capstone.catpedigreebase.data.local.repository

import android.content.Context
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.data.local.preferences.SharedPrefUserLogin
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.UserRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.utils.error.AuthError

open class UserRepository(
    private val context: Context,
    private val userRemote: UserRemoteDataSource,
    private val prefs: SharedPrefUserLogin
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
                        password = password,
                        bio = it.user?.bio,
                        lat = it.user?.lat,
                        lon = it.user?.lon,
                        profile_photo_path = it.user?.profile_photo_path,
                        token = it.token,
                        isLoggedIn = true
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

    suspend fun logout() {
        try {
            prefs.updateUser(UserItems(token = null, isLoggedIn = false))
        } catch (e: Throwable) {
            throw AuthError(e.message.toString())
        }
    }
}