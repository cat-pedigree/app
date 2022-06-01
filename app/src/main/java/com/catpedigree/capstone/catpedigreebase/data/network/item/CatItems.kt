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
    val name: String? = null,

    @field:SerializedName("breed")
    val breed: String? = null,

    @field:SerializedName("gender")
    val gender: String? = null,

    @field:SerializedName("color")
    val color: String? = null,

    @field:SerializedName("weight")
    val weight: Double? = null,

    @field:SerializedName("age")
    val age: Int? = null,

    @field:SerializedName("story")
    val story: String? = null,

    @field:SerializedName("photo")
    val photo: String? = null
) : Parcelable

