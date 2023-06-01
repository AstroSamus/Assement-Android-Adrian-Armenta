package com.example.codingchallenge.database

import android.content.Context
import android.graphics.Color
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

    //Use this to get only valid colors from the colorCode String
    val safeColorCode: Int
        get() {
            return try {
                Color.parseColor(colorCode)
            } catch (e: IllegalArgumentException) {
                //if the color is invalid then return green by default
                Color.GREEN
            }
        }
}
