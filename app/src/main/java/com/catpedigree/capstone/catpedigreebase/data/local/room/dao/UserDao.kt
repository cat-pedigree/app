package com.catpedigree.capstone.catpedigreebase.data.local.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserDataItems

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(users: List<UserDataItems>)

    @Query("SELECT * FROM user_items WHERE name LIKE '%' || :name || '%'")
    fun getSearch(name: String?): LiveData<List<UserDataItems>>

    @Query("SELECT * FROM user_items WHERE id = :id")
    fun getUser(id: Int?): LiveData<List<UserDataItems>>

    @Query("DELETE FROM user_items")
    suspend fun deleteAllUsers()
}