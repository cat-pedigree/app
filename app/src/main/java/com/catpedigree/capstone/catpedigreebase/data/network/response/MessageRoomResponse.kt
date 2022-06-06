package com.catpedigree.capstone.catpedigreebase.data.network.response

import com.google.gson.annotations.SerializedName

data class MessageRoomResponse(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<MessageRoomData>? = null,
)

data class MessageRoomData(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("receiver_user_id")
    val receiver_user_id: Int? = null,

    @field:SerializedName("sender_user_id")
    val sender_user_id: Int? = null,

    @field:SerializedName("messages")
    val messages: String? = null,

    @field:SerializedName("user")
    val user: UserMessageRoomData? = null,
)

data class UserMessageRoomData(
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("profile_photo_path")
    val profile_photo_path: String? = null,
)