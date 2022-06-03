package com.catpedigree.capstone.catpedigreebase.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.catpedigree.capstone.catpedigreebase.data.local.remote.source.FollowRemoteDataSource
import com.catpedigree.capstone.catpedigreebase.data.local.room.database.CatDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.FollowItems
import com.catpedigree.capstone.catpedigreebase.data.network.item.FollowingItems
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.error.FollowError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FollowRepository(
    private val followRemoteDataSource: FollowRemoteDataSource,
    private val catDatabase: CatDatabase
) {

    fun getFollow(token: String, user_id: Int): LiveData<Result<List<FollowItems>>> = liveData {
        emit(Result.Loading)
        try {
            val response = followRemoteDataSource.getFollow(token, user_id)
            val follows = response.body()?.data
            val newFollow = follows?.map { follow ->
                FollowItems(
                    follow.id,
                    follow.user_id,
                    follow.follower_id,
                    follow.follower?.name,
                    follow.follower?.username,
                    follow.follower?.profile_photo_path
                )
            }
            catDatabase.followDao().deleteAllFollows()
            catDatabase.followDao().insertFollow(newFollow!!)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<FollowItems>>> =
            catDatabase.followDao().getFollower(user_id).map { Result.Success(it) }
        emitSource(dataLocal)
    }

    fun getFollowing(token: String, follower_id: Int): LiveData<Result<List<FollowingItems>>> = liveData {
        emit(Result.Loading)
        try {
            val response = followRemoteDataSource.getFollowing(token, follower_id)
            val follows = response.body()?.data
            val newFollow = follows?.map { follow ->
                FollowingItems(
                    follow.id,
                    follow.user_id,
                    follow.follower_id,
                    follow.following?.name,
                    follow.following?.username,
                    follow.following?.profile_photo_path
                )
            }
            catDatabase.followingDao().deleteAllFollowing()
            catDatabase.followingDao().insertFollowing(newFollow!!)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
        val dataLocal: LiveData<Result<List<FollowingItems>>> =
            catDatabase.followingDao().getFollowing(follower_id).map { Result.Success(it) }
        emitSource(dataLocal)
    }

    suspend fun postFollow(
        token: String,
        user_id: Int,
        follower_id: Int,
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    followRemoteDataSource.postFollow(
                        token,
                        user_id,
                        follower_id,
                    )

                if (!response.isSuccessful) {
                    throw FollowError(response.message())
                }
            } catch (e: Throwable) {
                throw FollowError(e.message.toString())
            }
        }
    }

    suspend fun followDelete(
        token: String,
        user_id: Int,
        follower_id: Int,
    ) {
        withContext(Dispatchers.IO) {
            try {
                val response =
                    followRemoteDataSource.followDelete(
                        token,
                        user_id,
                        follower_id
                    )

                if (!response.isSuccessful) {
                    throw FollowError(response.message())
                }
            } catch (e: Throwable) {
                throw FollowError(e.message.toString())
            }
        }
    }

    fun getCheckFollow(user_id: Int, follower_id: Int): LiveData<Int> {
        return catDatabase.followDao().getCheckFollow(user_id,follower_id)
    }

    fun checkFollow(user_id: Int, follower_id: Int): LiveData<Int>{
        return catDatabase.followDao().checkFollow(user_id, follower_id)
    }
}