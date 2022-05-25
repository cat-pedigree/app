package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.UserInterface

class UserRemoteDataSource(private val userInterface: UserInterface) {

    suspend fun login(email: String, password: String) = userInterface.login(email, password)

    suspend fun register(name: String, username: String, phone_number: String, email: String, password: String) =
        userInterface.register(name, username, phone_number, email, password)
}