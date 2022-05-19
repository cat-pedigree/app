package com.catpedigree.capstone.catpedigreebase.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.catpedigree.capstone.catpedigreebase.data.item.PostItems
import com.catpedigree.capstone.catpedigreebase.data.item.RemoteItems

@Database(entities = [PostItems::class, RemoteItems::class], version = 1, exportSchema = false)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun remoteItemsDao(): RemoteItemsDao

    companion object {
        @Volatile
        private var INSTANCE: PostDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): PostDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    PostDatabase::class.java, "cats_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}