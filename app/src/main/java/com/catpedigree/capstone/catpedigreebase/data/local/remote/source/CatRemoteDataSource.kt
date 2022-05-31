package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.CatInterface
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

    suspend fun getCat(
        token: String,
        user_id: Int
    ) = catInterface.getCat("Bearer $token", user_id)

    suspend fun catCreateAlbum(
        token: String,
        user_id: Int,
        cat_id: Int,
        photo: MultipartBody.Part
    ) = catInterface.catCreateAlbum("Bearer $token", user_id, cat_id,photo)

    suspend fun getAlbum(
        token: String,
        user_id: Int,
        cat_id: Int
    ) = catInterface.getCatAlbum("Bearer $token",user_id, cat_id)
}