package com.catpedigree.capstone.catpedigreebase.data.network.item

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "favorite_post")
data class FavoriteItems(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("profile_photo_path")
    val profile_photo_path: String? = null,

    @field:SerializedName("photo")
    val photo: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("loves_count")
    val loves_count: Int? = null,

    @field:SerializedName("comments_count")
    val comments_count: Int? = null
): Parcelable
