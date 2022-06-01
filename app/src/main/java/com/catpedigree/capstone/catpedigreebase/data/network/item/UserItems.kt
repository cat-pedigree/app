package com.catpedigree.capstone.catpedigreebase.data.network.item

data class UserItems(
    val id: Int? = null,
    val name: String? = null,
    val username: String? = null,
    val phone_number: String? = null,
    val email: String? = null,
    val bio: String? = null,
    val profile_photo_path: String? = null,
    val token: String? = null,
    var isLoggedIn: Boolean? = null,
    val postsCount: Int? = null,
    val catsCount: Int? = null,
)
