package com.catpedigree.capstone.catpedigreebase.data.network.item

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "post_items")
data class PostItems(

    @PrimaryKey
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("profile_photo_path")
    val profile_photo_path: String? = null,

    @field:SerializedName("photo")
    val photo: String? = null,

    @field:SerializedName("title")
    val title: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("created_at")
    val created_at: String? = null,

    @field:SerializedName("lat")
    val lat: Double? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @field:SerializedName("loves_count")
    val loves_count: Int? = null,

    @field:SerializedName("comments_count")
    val comments_count: Int? = null,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean
): Parcelable
