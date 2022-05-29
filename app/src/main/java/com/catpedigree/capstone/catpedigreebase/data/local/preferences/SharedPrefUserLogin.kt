package com.catpedigree.capstone.catpedigreebase.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SharedPrefUserLogin private constructor(private val dataStore: DataStore<Preferences>) {
        fun getUser(): Flow<UserItems> {
        return dataStore.data.map { preferences ->
            UserItems(
                id = preferences[ID_KEY],
                name = preferences[NAME_KEY],
                username = preferences[USERNAME_KEY],
                phone_number = preferences[PHONE_NUMBER_KEY],
                email = preferences[EMAIL_KEY],
                token = preferences[TOKEN_KEY],
                bio = preferences[BIO_KEY],
                lat = preferences[LAT_KEY],
                lon = preferences[LON_KEY],
                profile_photo_path = preferences[PROFILE_PHOTO_PATH_KEY],
                isLoggedIn = preferences[STATE_KEY],
                postsCount = preferences[POSTS_COUNT],
                catsCount = preferences[CATS_COUNT]
            )
        }
    }

    suspend fun updateUser(user: UserItems) {
        dataStore.edit { preferences ->
            user.id?.let { preferences[ID_KEY] = it }
            user.name?.let { preferences[NAME_KEY] = it }
            user.username?.let { preferences[USERNAME_KEY] = it }
            user.email?.let { preferences[EMAIL_KEY] = it }
            user.token?.let { preferences[TOKEN_KEY] = it }
            user.bio?.let { preferences[BIO_KEY] = it }
            user.lat?.let { preferences[LAT_KEY] = it }
            user.lon?.let { preferences[LON_KEY] = it }
            user.profile_photo_path?.let { preferences[PROFILE_PHOTO_PATH_KEY] = it }
            user.isLoggedIn?.let { preferences[STATE_KEY] = it }
            user.postsCount?.let { preferences[POSTS_COUNT] = it }
            user.catsCount?.let { preferences[CATS_COUNT] = it }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SharedPrefUserLogin? = null

        private val ID_KEY = intPreferencesKey("id")
        private val NAME_KEY = stringPreferencesKey("name")
        private val USERNAME_KEY = stringPreferencesKey("username")
        private val PHONE_NUMBER_KEY = stringPreferencesKey("phone_number")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val BIO_KEY = stringPreferencesKey("bio")
        private val LAT_KEY = doublePreferencesKey("lat")
        private val LON_KEY = doublePreferencesKey("lon")
        private val PROFILE_PHOTO_PATH_KEY = stringPreferencesKey("profile_photo_path")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")
        private val POSTS_COUNT = intPreferencesKey("posts_count")
        private val CATS_COUNT = intPreferencesKey("cats_count")

        fun getInstance(dataStore: DataStore<Preferences>): SharedPrefUserLogin {
            return INSTANCE ?: synchronized(this) {
                val instance = SharedPrefUserLogin(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}