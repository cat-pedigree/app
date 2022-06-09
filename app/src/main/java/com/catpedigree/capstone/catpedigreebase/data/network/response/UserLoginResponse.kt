package com.catpedigree.capstone.catpedigreebase.data.network.response

import com.google.gson.annotations.SerializedName

data class UserLoginResponse(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: UserLoginData? = null,
)

data class UserLoginData(
    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("user")
    val user: UserData? = null
)

data class UserData(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("bio")
    val bio: String? = null,

    @field:SerializedName("profile_photo_path")
    val profile_photo_path: String? = null,

    @field: SerializedName("posts_count")
    val posts_count: Int? = null,

    @field: SerializedName("cats_count")
    val cats_count: Int? = null,

    @field: SerializedName("followers_count")
    val followers_count: Int? = null,

    @field: SerializedName("following")
    val following: Int? = null,
)
