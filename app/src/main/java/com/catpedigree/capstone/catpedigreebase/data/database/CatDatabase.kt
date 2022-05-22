package com.catpedigree.capstone.catpedigreebase.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.catpedigree.capstone.catpedigreebase.data.item.CommentItems
import com.catpedigree.capstone.catpedigreebase.data.item.PostItems
import com.catpedigree.capstone.catpedigreebase.data.item.RemoteCommentItems
import com.catpedigree.capstone.catpedigreebase.data.item.RemoteItems

@Database(entities = [
    PostItems::class,
    CommentItems::class,
    RemoteItems::class,
    RemoteCommentItems::class],
    version = 1,
    exportSchema = false)
abstract class CatDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun remoteItemsDao(): RemoteItemsDao
    abstract fun remoteCommentItemsDao(): RemoteCommentItemsDao


    companion object {
        @Volatile
        private var INSTANCE: CatDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): CatDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    CatDatabase::class.java, "cats_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}