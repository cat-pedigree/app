package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.CatInterface
import com.catpedigree.capstone.catpedigreebase.data.network.api.service.PostInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CatRemoteDataSource(private val catInterface: CatInterface) {

    suspend fun catCreate(
        token: String,
        user_id: Int,
        name: RequestBody,
        breed: RequestBody,
        gender: RequestBody,
        color: RequestBody,
        weight:Double,
        age: Int,
        story:RequestBody,
        photo: MultipartBody.Part,
    ) = catInterface.catCreate("Bearer $token",user_id,name, breed, gender,color, weight, age, story, photo)
}