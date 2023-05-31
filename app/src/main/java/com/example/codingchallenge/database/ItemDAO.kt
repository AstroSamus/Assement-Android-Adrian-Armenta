package com.example.codingchallenge.database

import androidx.room.*

@Dao
interface ItemDAO {
    @Insert
    suspend fun addItem(item: Item)

    @Query("SELECT * FROM item ORDER BY id DESC")
    suspend fun getAll(): List<Item>

    @Update
    suspend fun updateItem(item: Item)

    @Delete
    suspend fun deleteItem(item:Item)
}