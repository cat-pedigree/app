package com.catpedigree.capstone.catpedigreebase.data.local.remote.source

import com.catpedigree.capstone.catpedigreebase.data.network.api.service.FollowInterface

class FollowRemoteDataSource(private val followInterface: FollowInterface) {

    suspend fun getFollow(token: String, user_id: Int) =
        followInterface.getFollow("Bearer $token", user_id)

    suspend fun getFollowing(token: String, follower_id: Int) =
        followInterface.getFollowing("Bearer $token", follower_id)

    suspend fun postFollow(
        token: String,
        user_id: Int,
        follower_id: Int,
    ) = followInterface.postFollow("Bearer $token", user_id, follower_id)

    suspend fun followDelete(
        token: String,
        user_id: Int,
        follower_id: Int,
    ) = followInterface.followDelete("Bearer $token", user_id,follower_id)
}