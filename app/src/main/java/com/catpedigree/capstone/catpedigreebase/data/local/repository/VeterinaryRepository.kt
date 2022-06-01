package com.catpedigree.capstone.catpedigreebase.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.VeterinaryRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.VeterinaryItems
import com.catpedigree.capstone.catpedigreebase.utils.Result

class VeterinaryRepository(
    private val veterinaryRemoteDataSource: VeterinaryRemoteDataSource,
    private val catDatabase: CatDatabase
) {

    fun getVeterinary(token: String): LiveData<Result<List<VeterinaryItems>>> = liveData {
        emit(Result.Loading)
        try{
            val response = veterinaryRemoteDataSource.getVeterinary(token)
            val veterinaries = response.body()?.data
            val newVeterinary = veterinaries?.map { veterinary ->
                VeterinaryItems(
                    veterinary.id,
                    veterinary.name,
                    veterinary.city,
                    veterinary.country,
                    veterinary.lat,
                    veterinary.lon
                )
            }
            catDatabase.veterinaryDao().deleteAllVeterinary()
            catDatabase.veterinaryDao().insertVeterinary(newVeterinary!!)
        }catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<VeterinaryItems>>> = catDatabase.veterinaryDao().getVeterinary().map { Result.Success(it) }
        emitSource(dataLocal)
    }
}