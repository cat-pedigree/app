package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.VeterinaryInterface

class VeterinaryRemoteDataSource(private val veterinaryInterface: VeterinaryInterface) {

    suspend fun getVeterinary(
        token: String,
    ) = veterinaryInterface.getVeterinary("Bearer $token")
}