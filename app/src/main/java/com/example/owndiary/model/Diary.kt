package com.example.owndiary.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

//@Entity(tableName = "diary_table")
data class Diary(
    var image: Bitmap?,
    var title: String,
    var content: String,
    var isFavorite: Boolean = false,
    var date: LocalDateTime = LocalDateTime.now(),
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}