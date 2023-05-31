package com.example.codingchallenge.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.codingchallenge.data.Item

class ItemViewModel : ViewModel() {
    var itemsData = MutableLiveData<MutableList<Item>?>()

    init {
        itemsData.value = mutableListOf()
    }


    fun addItem(newItem: Item) {
        itemsData.value?.add(newItem)
    }
}