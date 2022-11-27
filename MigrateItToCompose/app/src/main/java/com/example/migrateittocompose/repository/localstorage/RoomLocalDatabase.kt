package com.example.migrateittocompose.repository.localstorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserId::class], version = 1)
abstract class RoomLocalDatabase: RoomDatabase() {
    abstract fun getRoomDAO(): RoomDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: RoomLocalDatabase? = null

        fun getDatabase(context: Context): RoomLocalDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomLocalDatabase::class.java,
                    "word_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}