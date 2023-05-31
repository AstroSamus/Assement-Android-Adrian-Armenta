package com.example.codingchallenge.database

import android.content.Context
import android.provider.ContactsContract
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities =  [Item::class],
    version = 1,
    exportSchema = false
)
abstract class ItemDatabase: RoomDatabase() {
    abstract fun getItemDao(): ItemDAO

    companion object {
        @Volatile private var instance: ItemDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            ItemDatabase::class.java,
            "itemdatabase"
        ).build()
    }
}