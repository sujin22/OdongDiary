package com.example.owndiary.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.owndiary.OwnDiaryApplication.Companion.sharedPreferences
import com.example.owndiary.model.database.DiaryDao
import com.example.owndiary.model.Diary
import com.example.owndiary.model.PaletteItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.io.IOException
import java.text.FieldPosition
import javax.inject.Inject


class DiaryRepository @Inject constructor(private val diaryDao: DiaryDao){
    //Room
    fun getAllDiary() : Flow<List<Diary>> =
        diaryDao.getAll().flowOn(Dispatchers.IO).conflate()

    fun getDiaryPage(position: Int, size: Int) = diaryDao.getAll(position, size)

    suspend fun getDiaryById(id: Int): Diary? = diaryDao.getDiaryById(id)
    suspend fun addDiary(diary: Diary) = diaryDao.insert(diary)
    suspend fun updateDiary(diary: Diary) = diaryDao.update(diary)
    suspend fun deleteDiary(diary: Diary) = diaryDao.delete(diary)
    suspend fun deleteAllDiary() = diaryDao.deleteAll()

    //SharedPreferences
    fun getDiaryName(): String = sharedPreferences.getDiaryName()
    fun getThemeColor(): PaletteItem = sharedPreferences.getThemeColor()

    fun setDiaryName(name: String) = sharedPreferences.setDiaryName(name)
    fun setThemeColor(item: PaletteItem) = sharedPreferences.setThemeColor(item)
}