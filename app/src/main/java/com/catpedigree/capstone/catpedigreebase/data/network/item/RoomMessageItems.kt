package com.catpedigree.capstone.catpedigreebase.data.network.item

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "message_room_items")
data class RoomMessageItems(

    @PrimaryKey
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("receiver_user_id")
    val receiver_user_id: Int? = null,

    @field:SerializedName("sender_user_id")
    val sender_user_id: Int? = null,

    @field:SerializedName("messages")
    val messages: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("profile_photo_path")
    val profile_photo_path: String? = null
) : Parcelable