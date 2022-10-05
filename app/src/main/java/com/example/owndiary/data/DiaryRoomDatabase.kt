package com.example.owndiary.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.owndiary.model.Diary
@Database(
    entities = [Diary::class], version = 1, exportSchema = false
)
@TypeConverters(RoomTypeConverter::class)
abstract class DiaryRoomDatabase: RoomDatabase() {
    abstract fun diaryDao(): DiaryDao
}