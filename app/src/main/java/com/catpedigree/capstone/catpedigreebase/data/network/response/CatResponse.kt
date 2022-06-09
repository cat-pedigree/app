package com.catpedigree.capstone.catpedigreebase.data.network.response

import com.google.gson.annotations.SerializedName

data class CatResponse(
    @field:SerializedName("code")
    val code: Int? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("data")
    val data: List<CatData>? = null,
)

data class CatData(
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

    @field:SerializedName("lat")
    val lat: Double? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @field:SerializedName("isWhite")
    val isWhite: Int,

    @field:SerializedName("story")
    val story: String? = null,
)