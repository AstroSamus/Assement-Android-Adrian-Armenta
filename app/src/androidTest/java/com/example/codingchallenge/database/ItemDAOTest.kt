package com.example.codingchallenge.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat

@RunWith(AndroidJUnit4::class)
@SmallTest
class ItemDAOTest: TestCase() {

    private lateinit var dataBase : ItemDatabase
    private lateinit var preFilledDataBase : ItemDatabase
    private lateinit var  itemDao: ItemDAO
    val firstItem = Item("First", "RED", "image1")

    @Before
    fun createDB() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        dataBase = Room.inMemoryDatabaseBuilder(
            context,
            ItemDatabase::class.java
        )
        .build()

        itemDao = dataBase.getItemDao()
    }



    @After
    fun closeDB() {
        dataBase.close()
    }

    //Write an new item in the db and read from it, the result should contain the new item
    @Test
    fun writeItemAndReadInList() = runBlocking {
        val testItem = Item("test", "GREEN", "imageTest")
        itemDao.addItem(testItem)
        val itemsInDB = itemDao.getAll()
        assertThat(itemsInDB.contains(testItem)).isTrue()
    }


}