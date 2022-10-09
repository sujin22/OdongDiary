package com.example.owndiary.model.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class RoomTypeConverter {
    //Bitmap -> ByteArray
    @TypeConverter
    fun toByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }

    //ByteArray -> Bitmap
    @TypeConverter
    fun toBitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    //LocalDateTime -> Long
    @TypeConverter
    fun fromDate(date: LocalDateTime): Long {
        return ZonedDateTime.of(date, ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    //Long -> LocalDateTime
    @TypeConverter
    fun toLocalDate(millisSinceEpoch: Long): LocalDateTime {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(millisSinceEpoch),
            TimeZone.getDefault().toZoneId()
        )
    }
}