package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.UserInterface
import okhttp3.MultipartBody

class UserRemoteDataSource(private val userInterface: UserInterface) {

    suspend fun login(email: String, password: String) = userInterface.login(email, password)

    suspend fun register(
        name: String,
        username: String,
        email: String,
        password: String
    ) =
        userInterface.register(name, username, email, password)

    suspend fun search(token: String, name: String) = userInterface.search("Bearer $token", name)

    suspend fun getUser(token: String, id: Int) = userInterface.getUser("Bearer $token", id)

    suspend fun profile(
        token: String,
        name: String,
        username: String,
        bio: String,
    ) = userInterface.profile("Bearer $token", name, username, bio)


    suspend fun change(
        token: String,
        profile_photo_path: MultipartBody.Part,
    ) = userInterface.change("Bearer $token", profile_photo_path)

    suspend fun changeEmail(
        token: String,
        email: String
    ) = userInterface.changeEmail("Bearer $token", email)

    suspend fun changePassword(
        token: String,
        password: String
    ) = userInterface.changePassword("Bearer $token", password)

    suspend fun userDelete(
        token: String,
        id: Int
    ) = userInterface.userDelete("Bearer $token", id)
}