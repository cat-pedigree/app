package com.catpedigree.capstone.catpedigreebase.data.network.api.service

import com.catpedigree.capstone.catpedigreebase.data.network.response.VeterinaryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface VeterinaryInterface {
    @GET("veterinary")
    suspend fun getVeterinary(
        @Header("Authorization") token: String,
    ): Response<VeterinaryResponse>
}