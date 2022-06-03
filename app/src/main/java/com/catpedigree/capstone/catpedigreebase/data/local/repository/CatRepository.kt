package com.catpedigree.capstone.catpedigreebase.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.CatRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.*
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.error.CatError
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
        weight: Double,
        age: Int,
        story: RequestBody,
        photo: MultipartBody.Part,
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    catRemoteDataSource.catCreate(
                        token, user_id, name, breed, gender, color, weight, age, story, photo
                    )
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
                CatItems(
                    cat.id,
                    cat.user_id,
                    cat.name,
                    cat.breed,
                    cat.gender,
                    cat.color,
                    cat.weight,
                    cat.age,
                    cat.story,
                    cat.photo,
                )
            }
//            catDatabase.catDao().deleteAllCats()
            catDatabase.catDao().insertCat(newCat!!)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<CatItems>>> =
            catDatabase.catDao().getCats(user_id).map { Result.Success(it) }
        emitSource(dataLocal)
    }

    fun checkCat(user_id: Int): LiveData<Int>{
        return catDatabase.catDao().checkCat(user_id)
    }

    suspend fun catCreateAlbum(
        token: String,
        user_id: Int,
        cat_id: Int,
        photo: MultipartBody.Part,
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    catRemoteDataSource.catCreateAlbum(
                        token, user_id, cat_id, photo
                    )
                if (!response.isSuccessful) {
                    throw CatError(response.message())
                }
            } catch (e: Throwable) {
                throw CatError(e.message.toString())
            }
        }
    }

    fun getAlbum(token: String, user_id: Int, cat_id: Int): LiveData<Result<List<AlbumItems>>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = catRemoteDataSource.getAlbum(token, user_id, cat_id)
                val cats = response.body()?.data
                val newCat = cats?.map { album ->
                    AlbumItems(
                        album.id,
                        album.user_id,
                        album.cat_id,
                        album.photo,
                    )
                }
                catDatabase.catDao().deleteAllAlbums()
                catDatabase.catDao().insertAlbum(newCat!!)
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
            val dataLocal: LiveData<Result<List<AlbumItems>>> =
                catDatabase.catDao().getAlbums(user_id, cat_id).map { Result.Success(it) }
            emitSource(dataLocal)
        }
}