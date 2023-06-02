package com.example.codingchallenge.database

import android.graphics.Color
import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Item(
    var name: String,
    var colorCode: String,
    var image: String,
): Parcelable {
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
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
        id = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(colorCode)
        parcel.writeString(image)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }

}
