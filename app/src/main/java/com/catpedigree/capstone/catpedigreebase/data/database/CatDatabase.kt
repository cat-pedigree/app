package com.catpedigree.capstone.catpedigreebase.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.catpedigree.capstone.catpedigreebase.data.item.*

@Database(entities = [
    PostItems::class,
    CommentItems::class,
    RemoteItems::class,
    RemoteCommentItems::class,
    FavoriteItems::class,
    LoveItems::class],
    version = 1,
    exportSchema = false)
abstract class CatDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun remoteItemsDao(): RemoteItemsDao
    abstract fun remoteCommentItemsDao(): RemoteCommentItemsDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun loveDao(): LoveDao


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