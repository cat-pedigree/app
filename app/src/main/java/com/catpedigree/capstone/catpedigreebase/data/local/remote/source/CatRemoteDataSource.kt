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
        eye_color: RequestBody,
        hair_color: RequestBody,
        ear_shape: RequestBody,
        weight: Double,
        age: Int,
        photo: MultipartBody.Part,
        isWhite: Int,
        story: RequestBody,
        lat: Double? = null,
        lon: Double? = null,
    ) = catInterface.catCreate(
        "Bearer $token",
        user_id,
        name,
        breed,
        gender,
        color,
        eye_color,
        hair_color,
        ear_shape,
        weight,
        age,
        photo,
        isWhite,
        story,
        lat,
        lon,
    )

    suspend fun getCat(
        token: String,
        user_id: Int
    ) = catInterface.getCat("Bearer $token", user_id)

    suspend fun getCatFilter(
        token: String,
        breed:String,
        color:String,
        gender: String,
        isWhite:Int,
    ) = catInterface.getCatFilter("Bearer $token", breed,color,gender,isWhite)

    suspend fun getCatLocation(
        token: String
    ) = catInterface.getCatLocation("Bearer $token")
}