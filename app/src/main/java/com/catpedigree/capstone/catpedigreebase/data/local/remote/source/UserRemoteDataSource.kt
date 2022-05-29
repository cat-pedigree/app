package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.UserInterface
import okhttp3.MultipartBody

class UserRemoteDataSource(private val userInterface: UserInterface) {

    suspend fun login(email: String, password: String) = userInterface.login(email, password)

    suspend fun register(name: String, username: String, phone_number: String, email: String, password: String) =
        userInterface.register(name, username, phone_number, email, password)

    suspend fun profile(
        token: String,
        name:String,
        username: String,
        bio: String,
    )= userInterface.profile("Bearer $token", name, username,bio)


    suspend fun change(
        token: String,
        profile_photo_path: MultipartBody.Part,
    ) = userInterface.change("Bearer $token",profile_photo_path)
}