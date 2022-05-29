package com.catpedigree.capstone.catpedigreebase.data.local.repository

import android.content.Context
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.CatRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.utils.error.CatError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class CatRepository(
    private val catRemoteDataSource: CatRemoteDataSource,
) {
    suspend fun catCreate(
        token: String,
        user_id: Int,
        name: String,
        breed: String,
        gender: String,
        color: String,
        weight:Double,
        age: Int,
        story:String,
        photo: MultipartBody.Part,
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    catRemoteDataSource.catCreate(
                        token, user_id,name, breed, gender, color, weight, age, story, photo
                    )
                if (!response.isSuccessful) {
                    throw CatError(response.message())
                }
            } catch (e: Throwable) {
                throw CatError(e.message.toString())
            }
        }
    }
}