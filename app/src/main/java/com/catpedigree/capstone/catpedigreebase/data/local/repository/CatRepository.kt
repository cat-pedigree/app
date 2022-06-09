package com.catpedigree.capstone.catpedigreebase.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.CatRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.*
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.error.CatError
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import okhttp3.RequestBody

class CatRepository(
    private val catRemoteDataSource: CatRemoteDataSource,
    private val catDatabase: CatDatabase
) {
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
        latLng: LatLng?,
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    if(latLng != null){
                        catRemoteDataSource.catCreate(
                            token,
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
                            latLng.latitude,
                            latLng.longitude
                        )
                    }else{
                        catRemoteDataSource.catCreate(
                            token,
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
                            story
                        )
                    }

                if (!response.isSuccessful) {
                    throw CatError(response.message())
                }
            } catch (e: Throwable) {
                throw CatError(e.message.toString())
            }
        }
    }

    fun getCat(token: String, user_id: Int): LiveData<Result<List<CatItems>>> = liveData {
        emit(Result.Loading)
        try {
            val response = catRemoteDataSource.getCat(token, user_id)
            val cats = response.body()?.data
            val newCat = cats?.map { cat ->
                val isCatSelected = catDatabase.catDao().isCatSelected(cat.id!!)
                CatItems(
                    cat.id,
                    cat.user_id,
                    cat.name,
                    cat.breed,
                    cat.gender,
                    cat.color,
                    cat.eye_color,
                    cat.hair_color,
                    cat.ear_shape,
                    cat.weight,
                    cat.age,
                    cat.photo,
                    cat.isWhite,
                    cat.story,
                    isCatSelected,
                    cat.lat,
                    cat.lon
                )
            }
            catDatabase.catDao().deleteAllCats()
            catDatabase.catDao().insertCat(newCat!!)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<CatItems>>> =
            catDatabase.catDao().getCats(user_id).map { Result.Success(it) }
        emitSource(dataLocal)
    }

    fun getCatFilter(
        token: String,
        breed: String,
        color:String,
        gender: String,
        isWhite: Int
    ): LiveData<Result<List<CatItems>>> = liveData {
        emit(Result.Loading)
        try {
            val response = catRemoteDataSource.getCatFilter(token,breed,color,gender,isWhite)
            val cats = response.body()?.data
            val newCat = cats?.map { cat ->
                val isCatSelected = catDatabase.catDao().isCatSelected(cat.id!!)
                CatItems(
                    cat.id,
                    cat.user_id,
                    cat.name,
                    cat.breed,
                    cat.gender,
                    cat.color,
                    cat.eye_color,
                    cat.hair_color,
                    cat.ear_shape,
                    cat.weight,
                    cat.age,
                    cat.photo,
                    cat.isWhite,
                    cat.story,
                    isCatSelected,
                    cat.lat,
                    cat.lon
                )
            }
            catDatabase.catDao().deleteAllCats()
            catDatabase.catDao().insertCat(newCat!!)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<CatItems>>> =
            catDatabase.catDao().allCats().map { Result.Success(it) }
        emitSource(dataLocal)
    }

    suspend fun setCatSelected(cat: CatItems, catSelected: Boolean) {
        cat.isSelected = catSelected
        catDatabase.catDao().updateCat(cat)
    }

    fun getCatSelected(): LiveData<CatItems> {
        return catDatabase.catDao().getCatSelected()
    }

    suspend fun getCatLocation(token: String) :List<CatItems>{
        val cats = mutableListOf<CatItems>()
        withContext(Dispatchers.IO){
            try {
                val response = catRemoteDataSource.getCatLocation(token)
                if (response.isSuccessful) {
                    response.body()?.data?.forEach {
                        cats.add(
                            CatItems(
                                id = it.id,
                                user_id = it.user_id,
                                name = it.name,
                                breed = it.breed,
                                gender = it.gender,
                                color = it.color,
                                eye_color = it.eye_color,
                                hair_color = it.hair_color,
                                ear_shape = it.ear_shape,
                                weight = it.weight,
                                age = it.age,
                                photo = it.photo,
                                isWhite = it.isWhite,
                                story = it.story,
                                false,
                                lat = it.lat,
                                lon = it.lon
                            )
                        )
                    }
                }
            } catch (e: Throwable) {
                throw CatError(e.message.toString())
            }
        }
        return cats
    }

    fun checkCat(user_id: Int): LiveData<Int>{
        return catDatabase.catDao().checkCat(user_id)
    }
}