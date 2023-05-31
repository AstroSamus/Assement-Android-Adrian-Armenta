package com.example.codingchallenge.data

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.codingchallenge.R
import java.net.URI
import java.util.*

data class Item(
    var name: String,
    var colorCode: String,
    var image: URI,
    var id: UUID = UUID.randomUUID()
)
