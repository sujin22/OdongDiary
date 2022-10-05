package com.example.owndiary.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

class RoomTypeConverter {
    //Bitmap -> ByteArray
    @TypeConverter
    fun toByteArray(bitmap: Bitmap): ByteArray{
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    //ByteArray -> Bitmap
    fun toBitmap(bytes: ByteArray) : Bitmap{
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}