package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.CatInterface
import com.catpedigree.capstone.catpedigreebase.data.network.api.service.VeterinaryInterface
import okhttp3.MultipartBody
import okhttp3.RequestBody

class VeterinaryRemoteDataSource(private val veterinaryInterface: VeterinaryInterface) {

    suspend fun getVeterinary(
        token: String,
    ) = veterinaryInterface.getVeterinary("Bearer $token")
}