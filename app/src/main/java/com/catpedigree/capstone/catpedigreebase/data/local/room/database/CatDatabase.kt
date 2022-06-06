package com.catpedigree.capstone.catpedigreebase.data.local.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.catpedigree.capstone.catpedigreebase.data.network.item.*
import com.catpedigree.capstone.catpedigreebase.data.local.room.dao.*

@Database(
    entities = [
        PostItems::class,
        CommentItems::class,
        CatItems::class,
        UserDataItems::class,
        VeterinaryItems::class,
        FollowItems::class,
        FollowingItems::class,
        MessageRoomItems::class],
    version = 1,
    exportSchema = false
)
abstract class CatDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun commentDao(): CommentDao
    abstract fun catDao(): CatDao
    abstract fun userDao(): UserDao
    abstract fun veterinaryDao(): VeterinaryDao
    abstract fun followDao(): FollowDao
    abstract fun followingDao(): FollowingDao
    abstract fun messageRoomDao(): MessageRoomDao

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