package com.example.codingchallenge.data

import android.content.Context
import android.net.Uri
import androidx.core.content.ContextCompat
import com.example.codingchallenge.R
import java.net.URI
import java.util.*

data class Item(
    var name: String,
    var colorCode: String,
    var image: Uri,
    var id: UUID = UUID.randomUUID()
)
