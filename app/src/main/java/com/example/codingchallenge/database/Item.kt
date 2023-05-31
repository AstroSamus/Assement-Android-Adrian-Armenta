package com.example.codingchallenge.database

import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.codingchallenge.R
import java.net.URI
import java.util.*
@Entity
data class Item(
    var name: String,
    var colorCode: String,
    var image: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
