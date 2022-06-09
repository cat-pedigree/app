package com.catpedigree.capstone.catpedigreebase.data.network.item

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "cat_items")
data class CatItems(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("user_id")
    val user_id: Int? = null,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("breed")
    val breed: String? = null,

    @field:SerializedName("gender")
    val gender: String,

    @field:SerializedName("color")
    val color: String,

    @field:SerializedName("eye_color")
    val eye_color: String? = null,

    @field:SerializedName("hair_color")
    val hair_color: String? = null,

    @field:SerializedName("ear_shape")
    val ear_shape: String? = null,

    @field:SerializedName("weight")
    val weight: Double? = null,

    @field:SerializedName("age")
    val age: Int? = null,

    @field:SerializedName("photo")
    val photo: String,

    @field:SerializedName("isWhite")
    val isWhite: Int,

    @field:SerializedName("story")
    val story: String? = null,

    @field:SerializedName("isSelected")
    var isSelected: Boolean,

    @field:SerializedName("lat")
    val lat: Double? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,
) : Parcelable

