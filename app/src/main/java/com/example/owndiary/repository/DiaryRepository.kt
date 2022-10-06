package com.example.owndiary.repository

import com.example.owndiary.data.DiaryDao
import com.example.owndiary.model.Diary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class DiaryRepository @Inject constructor(private val diaryDao: DiaryDao){
    fun getAllDiary() : Flow<List<Diary>> =
        diaryDao.getAll().flowOn(Dispatchers.IO).conflate()

    suspend fun getDiaryById(id: Int): Diary? = diaryDao.getDiaryById(id)
    suspend fun addDiary(diary: Diary) = diaryDao.insert(diary)
    suspend fun updateDiary(diary: Diary) = diaryDao.update(diary)
    suspend fun deleteDiary(diary: Diary) = diaryDao.delete(diary)
    suspend fun deleteAllDiary() = diaryDao.deleteAll()
}